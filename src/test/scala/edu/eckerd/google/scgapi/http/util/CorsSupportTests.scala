package edu.eckerd.google.scgapi.http.util

import akka.http.scaladsl.model.headers.Origin
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{FlatSpec, Matchers}
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ Directive0, Route }

/**
  * Created by davenpcm on 9/11/16.
  */
class CorsSupportTests extends FlatSpec with Matchers with ScalatestRouteTest with CorsSupport{

  val route = corsHandler {
    pathPrefix("cors"){
      pathEndOrSingleSlash {
        get {
          complete("Simple Response")
        }
      }
    }
  }

  "CorsHandler" should "add `Access-Control-Allow-Origin` header" in {
    Get("/cors") ~> route ~> check {
      header("Access-Control-Allow-Origin").get shouldBe `Access-Control-Allow-Origin`.*
    }
  }

  it should "add `Access-Control-Allow-Credentials` header" in {
    Get("/cors") ~> route ~> check {
      header("Access-Control-Allow-Credentials").get shouldBe `Access-Control-Allow-Credentials`(true)
    }
  }

  it should "add `Access-Control-Allow-Headers` header and allow only Token, Content-Type, and X-Requested-With" in {
    Get("/cors") ~> route ~> check {
      header("Access-Control-Allow-Headers").get shouldBe
        `Access-Control-Allow-Headers`("Token", "Content-Type", "X-Requested-With")
    }
  }

  it should "add `Access-Control-Allow-Methods` and allow only GET, POST, PUT, and DELETE" in {
    Options("/cors") ~> route ~> check {
      header("Access-Control-Allow-Methods").get shouldBe
        `Access-Control-Allow-Methods`(OPTIONS, POST, PUT, GET, DELETE)
    }
  }

}
