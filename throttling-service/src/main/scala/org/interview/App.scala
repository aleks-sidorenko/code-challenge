package org.interview

import akka.actor._
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.{ Config, ConfigFactory }
import com.typesafe.scalalogging.LazyLogging

object Main extends LazyLogging with App with Configuration with HttpService {

  override def config: Config = ConfigFactory.load()

  implicit val actorSystem = ActorSystem(appName)
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = actorSystem.dispatcher

  val binding = Http().bindAndHandle(route, httpService.interface, httpService.port)

  logger.info(s"Bound to port ${httpService.interface} on interface ${httpService.port}")

  binding onFailure {
    case ex: Exception ⇒
      logger.error(s"Failed to bind to ${httpService.interface}:${httpService.port}!", ex)
  }
  sys.addShutdownHook(actorSystem.terminate())
}

