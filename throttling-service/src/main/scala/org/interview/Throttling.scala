package org.interview

import Authentication.{Token, User, optionalHeaderValueByName, provide}
import akka.http.scaladsl.server.{Directive0, Directive1, Directives}
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Future

object Throttling {

  case class Sla(user: User, rps: Int)

  trait SlaService {

    def getSlaByUser(user: User): Future[Sla]

  }

  trait ThrottlingService {

    val graceRps: Int

    val slaService: SlaService

    def isRequestAllowed(user: User): Boolean
  }

  object ThrottlingService {
    def apply(graceRps: Int)
  }

  private[Throttling] class ThrottlingServiceImpl(override val graceRps: Int, override val slaService: SlaService)
    extends ThrottlingService {

    override def isRequestAllowed(user: User): Boolean = ???
  }


  object Throttler extends LazyLogging with Directives with Configuration {

    override def config =
    private[Throttler] val throttlingService = ThrottlingService()

    def throttling(user: User): Directive0 = {



      optionalHeaderValueByName("Authorization").flatMap {
        case Some(authHeader) =>
          val accessToken = authHeader.split(' ').last
          userRepository.getUserFromAccessToken(accessToken) match {
            case Some(user) => provide(user)
            case _ => provide(anonymous)
          }
        case _ => provide(anonymous)
      }
    }
  }

}