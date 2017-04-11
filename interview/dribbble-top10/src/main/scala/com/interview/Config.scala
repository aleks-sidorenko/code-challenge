package com.interview

import com.typesafe.config.{ Config, ConfigFactory }

trait Configuration {

  def config: Config

  private val appConfig = config.getConfig("app")

  val appName: String = appConfig.getString("name")

  object httpService {
    val interface = appConfig.getString("http-service.interface")
    val port = appConfig.getInt("http-service.port")
  }

  val accessToken = appConfig.getString("dribbble.access-token")

}

