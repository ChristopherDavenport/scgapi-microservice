package edu.eckerd.google.scgapi.services.auth

import akka.http.scaladsl.model.headers.BasicHttpCredentials
import akka.http.scaladsl.server.directives.Credentials
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by davenpcm on 9/10/16.
  */
class AuthServiceImplTests extends FlatSpec with Matchers {

  "authorize" should "return Some(User) with valid credentials" in {
    val password = "password"
    val user = "Chris Davenport"
    val authService = new AuthServiceImpl(password)
    val httpCredentials = BasicHttpCredentials(user, password)
    val validCredentials = Credentials(Some(httpCredentials))
    authService.authenticate(validCredentials) shouldEqual Some(user)
  }

  it should "return None with invalid credentials" in {
    val authService = new AuthServiceImpl("password")
    val httpCredentials = BasicHttpCredentials("Bad Boy", "notTheRightPassword")
    val invalidCredentials = Credentials(Some(httpCredentials))
    authService.authenticate(invalidCredentials) shouldEqual None
  }

  it should "return None with No Credentials" in {
    val authService = new AuthServiceImpl("password")
    val nonProvidedCredentials = Credentials(None)
    authService.authenticate(nonProvidedCredentials) shouldEqual None
  }

}
