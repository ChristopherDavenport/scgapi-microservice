package edu.eckerd.google.scgapi.http.routes

import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.{BasicHttpCredentials, HttpChallenge, `WWW-Authenticate`}
import akka.http.scaladsl.server.Route
import edu.eckerd.google.scgapi.models.Message
import edu.eckerd.google.scgapi.services.auth.AuthServiceImpl
import edu.eckerd.google.scgapi.http.util.JsonProtocol
import org.scalatest.Matchers
import org.scalatest.FlatSpec

/**
  * Created by Chris Davenport on 9/10/16.
  */
class TestServiceRoutesTests extends FlatSpec with Matchers with ScalatestRouteTest with JsonProtocol {

  val authService = new AuthServiceImpl("password")
  val testServiceRoutes = new TestServiceRoutes(authService)

  "The Non-Secure Test Service" should "return a response for GET requests to /test" in {
    Get("/test") ~> testServiceRoutes.route ~> check {
      responseAs[Message] shouldEqual Message("Insecure Get")
    }
  }

  it should "return a response to POST requests to /test" in {
    Post("/test") ~> testServiceRoutes.route ~> check {
      responseAs[Message] shouldEqual Message("Insecure Post")
    }
  }

  it should "return a response to PUT requests to /test" in {
    Put("/test") ~> testServiceRoutes.route ~> check {
      responseAs[Message] shouldEqual Message("Insecure Put")
    }
  }

  it should "return a response to DELETE requests to /test" in {
    Delete("/test") ~> testServiceRoutes.route ~> check {
      responseAs[Message] shouldEqual Message("Insecure Delete")
    }
  }

  "The Secure Test Service - GET" should "Reject insecure requests to /test/secure" in {
    Get("/test/secure") ~> Route.seal(testServiceRoutes.route) ~> check {
      status shouldEqual StatusCodes.Unauthorized
      responseAs[String] shouldEqual "The resource requires authentication, which was not supplied with the request"
      header[`WWW-Authenticate`].get.challenges.head shouldEqual HttpChallenge("Basic", Some("*"))
    }
  }

  it should "Handle valid credentials to /test/secure" in {
    val user = "Chris Davenport"
    val validCredentials = BasicHttpCredentials(user, "password")
    Get("/test/secure") ~> addCredentials(validCredentials) ~> Route.seal(testServiceRoutes.route) ~> check {
      responseAs[Message] shouldEqual Message(s"Secure Get By - $user")
    }
  }

  it should "Reject invalid credentials to /test/secure" in {
    val user = "Bad Boy"
    val invalidCredentials = BasicHttpCredentials(user, "notTheRightPassword")
    Get("/test/secure") ~> addCredentials(invalidCredentials) ~> Route.seal(testServiceRoutes.route) ~> check {
      status shouldEqual StatusCodes.Unauthorized
      responseAs[String] shouldEqual "The supplied authentication is invalid"
      header[`WWW-Authenticate`].get.challenges.head shouldEqual HttpChallenge("Basic", Some("*"))
    }
  }

  "The Secure Test Service - POST" should "Reject insecure requests to /test/secure" in {
    Post("/test/secure") ~> Route.seal(testServiceRoutes.route) ~> check {
      status shouldEqual StatusCodes.Unauthorized
      responseAs[String] shouldEqual "The resource requires authentication, which was not supplied with the request"
      header[`WWW-Authenticate`].get.challenges.head shouldEqual HttpChallenge("Basic", Some("*"))
    }
  }

  it should "Handle valid credentials to /test/secure" in {
    val user = "Chris Davenport"
    val validCredentials = BasicHttpCredentials(user, "password")
    Post("/test/secure") ~> addCredentials(validCredentials) ~> Route.seal(testServiceRoutes.route) ~> check {
      responseAs[Message] shouldEqual Message(s"Secure Post By - $user")
    }
  }

  it should "Reject invalid credentials to /test/secure" in {
    val user = "Bad Boy"
    val invalidCredentials = BasicHttpCredentials(user, "notTheRightPassword")
    Post("/test/secure") ~> addCredentials(invalidCredentials) ~> Route.seal(testServiceRoutes.route) ~> check {
      status shouldEqual StatusCodes.Unauthorized
      responseAs[String] shouldEqual "The supplied authentication is invalid"
      header[`WWW-Authenticate`].get.challenges.head shouldEqual HttpChallenge("Basic", Some("*"))
    }
  }

  "The Secure Test Service - PUT" should "Reject insecure requests to /test/secure" in {
    Put("/test/secure") ~> Route.seal(testServiceRoutes.route) ~> check {
      status shouldEqual StatusCodes.Unauthorized
      responseAs[String] shouldEqual "The resource requires authentication, which was not supplied with the request"
      header[`WWW-Authenticate`].get.challenges.head shouldEqual HttpChallenge("Basic", Some("*"))
    }
  }

  it should "Handle valid credentials to /test/secure" in {
    val user = "Chris Davenport"
    val validCredentials = BasicHttpCredentials(user, "password")
    Put("/test/secure") ~> addCredentials(validCredentials) ~> Route.seal(testServiceRoutes.route) ~> check {
      responseAs[Message] shouldEqual Message(s"Secure Put By - $user")
    }
  }

  it should "Reject invalid credentials to /test/secure" in {
    val user = "Bad Boy"
    val invalidCredentials = BasicHttpCredentials(user, "notTheRightPassword")
    Put("/test/secure") ~> addCredentials(invalidCredentials) ~> Route.seal(testServiceRoutes.route) ~> check {
      status shouldEqual StatusCodes.Unauthorized
      responseAs[String] shouldEqual "The supplied authentication is invalid"
      header[`WWW-Authenticate`].get.challenges.head shouldEqual HttpChallenge("Basic", Some("*"))
    }
  }

  "The Secure Test Service - DELETE" should "Reject insecure requests to /test/secure" in {
    Delete("/test/secure") ~> Route.seal(testServiceRoutes.route) ~> check {
      status shouldEqual StatusCodes.Unauthorized
      responseAs[String] shouldEqual "The resource requires authentication, which was not supplied with the request"
      header[`WWW-Authenticate`].get.challenges.head shouldEqual HttpChallenge("Basic", Some("*"))
    }
  }

  it should "Handle valid credentials to /test/secure" in {
    val user = "Chris Davenport"
    val validCredentials = BasicHttpCredentials(user, "password")
    Delete("/test/secure") ~> addCredentials(validCredentials) ~> Route.seal(testServiceRoutes.route) ~> check {
      responseAs[Message] shouldEqual Message(s"Secure Delete By - $user")
    }
  }

  it should "Reject invalid credentials to /test/secure" in {
    val user = "Bad Boy"
    val invalidCredentials = BasicHttpCredentials(user, "notTheRightPassword")
    Delete("/test/secure") ~> addCredentials(invalidCredentials) ~> Route.seal(testServiceRoutes.route) ~> check {
      status shouldEqual StatusCodes.Unauthorized
      responseAs[String] shouldEqual "The supplied authentication is invalid"
      header[`WWW-Authenticate`].get.challenges.head shouldEqual HttpChallenge("Basic", Some("*"))
    }
  }


}
