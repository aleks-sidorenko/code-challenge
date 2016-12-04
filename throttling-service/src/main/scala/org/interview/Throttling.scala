package org.interview

import java.util.concurrent.atomic.AtomicInteger

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.{Directive0, Directives}
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.LazyLogging
import org.interview.Authentication.User
import org.interview.Throttling.{CachingSlaService, SlaServiceImpl, ThrottlingService}
import scala.annotation.tailrec
import scala.concurrent.{ExecutionContext, Future}

object Throttling {

  final case class Sla(user: User, rps: Int)

  trait SlaService {
    def getSlaByUser(user: User): Future[Sla]
  }

  class SlaServiceImpl(implicit val executionContext: ExecutionContext) extends SlaService {
    override def getSlaByUser(user: User): Future[Sla] = Future {

      Sla(user, 10)
    }
  }

  final class CachingSlaService(val slaService: SlaService)(implicit val executionContext: ExecutionContext)
    extends SlaService {

    var slaCache: Map[User, Future[Sla]] = Map()

    override def getSlaByUser(user: User): Future[Sla] = {
      slaCache.get(user) match {
        case Some(sla) => sla
        case None =>
          val sla = slaService.getSlaByUser(user)
          slaCache = slaCache + (user -> sla)
          sla
      }
    }
  }


  trait ThrottlingService {

    val graceRps: Int

    val slaService: SlaService

    def isRequestAllowed(user: User): Boolean
  }

  object ThrottlingService {
    def apply(graceRps: Int, slaService: SlaService): ThrottlingService = {
      new ThrottlingServiceImpl(graceRps, slaService)
    }
  }

  private[Throttling] final class ThrottlingServiceImpl(override val graceRps: Int,
                                                        override val slaService: SlaService)
    extends ThrottlingService {

    import Authentication.anonymousUser

    private[this] var userRps: Map[User, RpsLimit] = Map(anonymousUser -> RpsLimit(graceRps))

    @tailrec
    override def isRequestAllowed(user: User): Boolean = {
      userRps.get(user) match {
        case Some(rpsLimit) => rpsLimit.allow()
        case _ =>
          val sla = slaService.getSlaByUser(user)
          if (sla.isCompleted) {
            sla.value match {
              case Some(scala.util.Success(s)) => {
                val limit = RpsLimit(s.rps)
                userRps = userRps + (s.user -> limit)
                limit.allow()
              }
              case _ => false
            }

          } else {
            isRequestAllowed(anonymousUser)
          }
      }
    }
  }

  case class RpsLimit(val rps: Int) {
    val allowedRps: AtomicInteger = new AtomicInteger(rps)

    def allow(): Boolean = {
      allowedRps.get() > 0
    }
  }

}

final object Throttler extends LazyLogging with Directives with Configuration {

  override def config: Config = ConfigFactory.load()

  private[Throttler] val throttlingService = ThrottlingService(
    throttling.graceRps,
    new CachingSlaService(new SlaServiceImpl))

  def throttle(user: User): Directive0 = {

    if (throttlingService.isRequestAllowed(user)) pass else complete(TooManyRequests)

  }

}

}