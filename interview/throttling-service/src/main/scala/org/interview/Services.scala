package org.interview

import akka.http.scaladsl.server.Directives
import com.typesafe.scalalogging.LazyLogging
import Authentication.authenticate
import Throttling.Throttler.throttle

trait HttpService extends LazyLogging with Directives with RandService {

  val route =
    pathPrefix("api") {
      path("rand") {
        get {
          authenticate { user =>
            throttle(user) {
              complete(rand().toString)
            }
          }
        }
      }
    }
}

trait RandService {
  def rand(): Int = scala.util.Random.nextInt()
}
