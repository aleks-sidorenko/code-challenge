package org.interview

import com.typesafe.config.{ Config }

trait Configuration {

  def config: Config

  private val appConfig = config.getConfig("app")

  val appName: String = appConfig.getString("name")

  object httpService {
    val interface = appConfig.getString("http-service.interface")
    val port = appConfig.getInt("http-service.port")
  }

  object throttling {
    val graceRps = appConfig.getInt("throttling.graceRps")
  }

}

