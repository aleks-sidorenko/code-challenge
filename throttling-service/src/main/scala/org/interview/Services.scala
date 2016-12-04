package org.interview

import akka.http.scaladsl.server.Directives
import com.typesafe.scalalogging.LazyLogging
import Authentication.authenticate
import Throttling._

trait HttpService extends LazyLogging with Directives with ThrottlingService {

  val route =
    pathPrefix("api") {
      path("rand") {
        get {
          authenticate { user =>
            complete(user)
          }
        }
      }
    }
}
