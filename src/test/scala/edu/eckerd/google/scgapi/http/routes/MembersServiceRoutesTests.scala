package edu.eckerd.google.scgapi.http.routes

import akka.http.scaladsl.model.headers.BasicHttpCredentials
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import edu.eckerd.google.scgapi.http.util.JsonProtocol
import edu.eckerd.google.scgapi.services.auth.AuthServiceImpl
import edu.eckerd.google.scgapi.services.core.members.MembersService
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by davenpcm on 9/12/16.
  */
class MembersServiceRoutesTests extends FlatSpec with Matchers with ScalatestRouteTest with JsonProtocol {


  class TestMemberService extends MembersService{

  }
  val authService = AuthServiceImpl("password")
  val membersService = new TestMemberService
  val groupsServiceRoutes = MembersServiceRoutes(membersService, authService)

  val user = "Chris Davenport"
  val validCredentials = BasicHttpCredentials(user, "password")
  val invalidCredential = BasicHttpCredentials(user, "basPassword")


  "Base Route Endpoint - GET" should "be handled" in {
    Get("/members") ~> addCredentials(validCredentials) ~> groupsServiceRoutes.route ~> check {
      handled shouldBe true
    }
  }
  "Group Prefix Endpoint - GET" should "be handled" in {
    Get("/members/groupPrefix") ~> addCredentials(validCredentials) ~> groupsServiceRoutes.route ~> check {
      handled shouldBe true
    }
  }

  "Group Prefix / Member Prefix Endpoint - GET" should "be handled" in {
    Get("/members/groupPrefix/memberPrefix") ~> addCredentials(validCredentials) ~> groupsServiceRoutes.route ~> check {
      handled shouldBe true
    }
  }

  "Endpoints Beyond Those Set Prefixes - GET" should "not be handled" in {
    Get("/members/groupPrefix/memberPrefix/nonsense") ~> addCredentials(validCredentials) ~> groupsServiceRoutes.route ~> check {
      handled shouldBe false
    }
  }

}
