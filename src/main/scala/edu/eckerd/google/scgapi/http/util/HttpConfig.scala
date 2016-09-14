package edu.eckerd.google.scgapi.http.util

import java.io.{File, FileInputStream, InputStream}
import java.security.{KeyStore, SecureRandom}
import javax.net.ssl.{KeyManagerFactory, SSLContext, TrustManagerFactory}

import akka.http.scaladsl.{ConnectionContext, HttpsConnectionContext}
import com.typesafe.config.ConfigFactory

/**
  * Created by Chris Davenport on 9/8/16.
  */
trait HttpConfig {
  lazy private[this] val config = ConfigFactory.load()
  lazy private[this] val httpConfig = config.getConfig("http")
  lazy val httpHost = httpConfig.getString("interface")
  lazy val httpPort = httpConfig.getInt("port")
  lazy val httpAccessPassword = httpConfig.getString("password")

  lazy private[this] val keystoreConfig = httpConfig.getConfig("ssl")
  lazy private[this] val httpsKeystorePassword: Array[Char] = keystoreConfig.getString("password").toCharArray
  lazy private[this] val httpsKeystoreType: String = keystoreConfig.getString("type")
  lazy private[this] val httpsKeystorePath: String = keystoreConfig.getString("path")



  private[this] def createHttpsConnectionContext(keyStoreType: String,
                                           keyStorePath: String,
                                           keyStorePassword: Array[Char]): HttpsConnectionContext = {

    val ks: KeyStore = KeyStore.getInstance("JKS")
    val keystore : InputStream = new FileInputStream(new File(keyStorePath))

    require(Option(keystore).isDefined, "Keystore required!")
    ks.load(keystore, keyStorePassword)

    keystore.close()

    val keyManagerFactory: KeyManagerFactory = KeyManagerFactory.getInstance("SunX509")
    keyManagerFactory.init(ks, keyStorePassword)

    val tmf: TrustManagerFactory = TrustManagerFactory.getInstance("SunX509")
    tmf.init(ks)

    val sslContext: SSLContext = SSLContext.getInstance("TLS")
    sslContext.init(keyManagerFactory.getKeyManagers, tmf.getTrustManagers, SecureRandom.getInstanceStrong)
    val https: HttpsConnectionContext = ConnectionContext.https(sslContext)
    https
  }

  lazy val https = createHttpsConnectionContext(httpsKeystoreType, httpsKeystorePath, httpsKeystorePassword)


}
