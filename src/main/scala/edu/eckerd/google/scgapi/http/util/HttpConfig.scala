package edu.eckerd.google.scgapi.http.util

import com.typesafe.config.ConfigFactory

/**
  * Created by davenpcm on 9/8/16.
  */
trait HttpConfig {
  lazy private val config = ConfigFactory.load()
  lazy private val httpConfig = config.getConfig("http")
  lazy val httpHost = httpConfig.getString("interface")
  lazy val httpPort = httpConfig.getInt("port")
  lazy val httpAccessPassword = httpConfig.getString("password")

//  val httpsKeystorePassword: Array[Char] = ???

}
