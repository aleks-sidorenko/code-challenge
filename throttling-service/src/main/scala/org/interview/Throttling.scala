package org.interview

import java.time.Duration
import java.util.concurrent.atomic.{ AtomicInteger, AtomicLong }

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.{ Directive0, Directives }
import com.typesafe.config.{ Config, ConfigFactory }
import com.typesafe.scalalogging.LazyLogging
import org.interview.Authentication.User

import scala.annotation.tailrec
import scala.concurrent.blocking
import scala.concurrent.{ ExecutionContext, Future }

object Throttling {

  final case class Sla(user: User, rps: Int)

  trait SlaService {
    def getSlaByUser(user: User): Future[Sla]
  }

  class SlaServiceImpl(val userRps: Int)(implicit val executionContext: ExecutionContext) extends SlaService {
    override def getSlaByUser(user: User): Future[Sla] = Future {
      blocking {
        Thread.sleep(250)
      }
      Sla(user, userRps)
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
    def apply(graceRps: Int, slaService: SlaService, renewRate: Double, renewInterval: Duration): ThrottlingService = {
      new ThrottlingServiceImpl(graceRps, slaService, renewRate, renewInterval)
    }
  }

  private[Throttling] final class ThrottlingServiceImpl(
    override val graceRps: Int,
    override val slaService: SlaService,
    val renewRate: Double,
    val renewInterval: Duration
  )
      extends ThrottlingService {

    import Authentication.anonymousUser

    private[this] var userRps: Map[User, RpsLimit] = Map(
      anonymousUser -> RpsLimit(graceRps, RpsRenewer((renewRate * graceRps).toInt, renewInterval))
    )

    @tailrec
    override def isRequestAllowed(user: User): Boolean = {
      userRps.get(user) match {
        case Some(rpsLimit) => rpsLimit.allow()
        case _ => {
          val sla = slaService.getSlaByUser(user)
          if (sla.isCompleted) {
            sla.value match {
              case Some(scala.util.Success(s)) => {
                val limit = RpsLimit(s.rps, RpsRenewer((s.rps * renewRate).toInt, renewInterval))
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
  }

  case class RpsRenewer(renewRps: Int, interval: Duration) {

    val lastRenewed: AtomicLong = new AtomicLong(System.currentTimeMillis())
    final val zeroRps: Int = 0

    @tailrec
    final def renewed(): Int = {
      val now = System.currentTimeMillis()
      val intervalMs = interval.toMillis

      val lastRenewed = this.lastRenewed.get()
      val nextRenew = lastRenewed + intervalMs

      if (now >= nextRenew) {
        if (this.lastRenewed.compareAndSet(lastRenewed, now)) {
          renewRps
        } else {
          renewed()
        }
      } else {
        zeroRps
      }
    }
  }

  case class RpsLimit(maxRps: Int, renewer: RpsRenewer) {
    val rps: AtomicInteger = new AtomicInteger(maxRps)

    final def allow(): Boolean = {
      val renewedRps = renewer.renewed()
      tryAllow(renewedRps)
    }

    @tailrec
    private final def tryAllow(renewedRps: Int): Boolean = {
      val currentRps = rps.get()
      val newRps = math.min(maxRps, currentRps + renewedRps)
      if (newRps > 0) {
        if (rps.compareAndSet(currentRps, newRps - 1)) {
          true
        } else {
          tryAllow(renewedRps)
        }
      } else {
        false
      }
    }
  }

  final object Throttler extends LazyLogging with Directives with Configuration {

    override def config: Config = ConfigFactory.load()

    import scala.concurrent.ExecutionContext.Implicits.global

    private[Throttler] val throttlingService = ThrottlingService(
      throttling.graceRps,
      new CachingSlaService(new SlaServiceImpl(throttling.userRps)),
      throttling.renewRate,
      throttling.renewInterval
    )

    def throttle(user: User): Directive0 = {

      if (throttlingService.isRequestAllowed(user)) pass else complete(TooManyRequests)

    }

  }

}