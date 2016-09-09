package edu.eckerd.google.scgapi.util

import com.typesafe.config.ConfigFactory

/**
  * Created by davenpcm on 9/8/16.
  */
trait HttpConfig {
  private val config = ConfigFactory.load()
  private val httpConfig = config.getConfig("http")
  val httpHost = httpConfig.getString("interface")
  val httpPort = httpConfig.getInt("port")
  val httpAccessPassword = httpConfig.getString("password")

//  val httpsKeystorePassword: Array[Char] = ???

}
