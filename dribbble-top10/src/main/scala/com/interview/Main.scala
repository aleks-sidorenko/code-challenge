package com.interview

import akka.actor._
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.interview.Dribbble.Api
import com.typesafe.config.{ Config, ConfigFactory }

object HttpServer extends App with Configuration with HttpService {

  override def config: Config = ConfigFactory.load()

  override implicit val system = ActorSystem(appName)
  override implicit val materializer = ActorMaterializer()
  override implicit val executor = system.dispatcher

  override def dribbble(): Api = Dribbble.Api(accessToken)

  val bindingFuture = Http().bindAndHandle(route, httpService.interface, httpService.port)

  println(s"HTTP server is listening on http://${httpService.interface}:${httpService.port}/")
}
