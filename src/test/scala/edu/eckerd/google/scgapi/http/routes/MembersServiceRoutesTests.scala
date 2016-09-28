package edu.eckerd.google.scgapi.http.routes

import akka.http.scaladsl.model.headers.BasicHttpCredentials
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import edu.eckerd.google.scgapi.http.util.JsonProtocol
import edu.eckerd.google.scgapi.models.MemberRoles.{MANAGER, MEMBER, OWNER}
import edu.eckerd.google.scgapi.models.MemberTypes.USER
import edu.eckerd.google.scgapi.models._
import edu.eckerd.google.scgapi.models.ErrorResponse._
import edu.eckerd.google.scgapi.services.auth.AuthServiceImpl
import edu.eckerd.google.scgapi.services.core.members.MembersService
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Future

/**
  * Created by Chris Davenport on 9/12/16.
  */
class MembersServiceRoutesTests extends FlatSpec with Matchers with ScalatestRouteTest with JsonProtocol {

  val completeMember = CompleteMember("cmEmail", "id", OWNER, USER)
  val matchedMember  = MatchedMember(None, Some("mmId"), MEMBER, USER)
  val memberBuilder = MemberBuilder("mbEmail", MANAGER, USER)


  class TestMemberService extends MembersService{
    def getMember   (groupEmail: String, memberEmail: String)   : Future[Either[ErrorResponse, Member]] = Future.successful{
      memberEmail match {
        case complete if complete == completeMember.email => Right(completeMember.validateEmail)
        case mb if mb == memberBuilder.email => Right(memberBuilder.validateEmail)
        case none  if none == "none" => Left(NotFound)
      }
    }

    def getMembers  (groupEmail: String)                        : Future[Either[ErrorResponse, Members]] =
      Future.successful(Right(Members(List(completeMember, matchedMember, memberBuilder))))

    def createMember(groupEmail: String, member: MemberBuilder) : Future[Either[ErrorResponse, Member]] = Future.successful {
      member.email match {
        case cm if cm == completeMember.email => Right(completeMember.validateEmail)
        case mb if mb == memberBuilder.email => Right(memberBuilder.validateEmail)
        case notFound if notFound == "none" => Left(NotFound)
      }
    }

    def deleteMember(groupEmail: String, memberEmail: String)   : Future[Either[ErrorResponse, Unit]] =
      Future.successful(Right(()))
  }
  val authService = AuthServiceImpl("password")
  val membersService = new TestMemberService
  val groupsServiceRoutes = MembersServiceRoutes(membersService, authService)

  val user = "Chris Davenport"
  val validCredentials = BasicHttpCredentials(user, "password")
  val invalidCredential = BasicHttpCredentials(user, "badPassword")


  "Base Route Endpoint - GET" should "be handled" in {
    Get("/groups/groupPrefix/members") ~> addCredentials(validCredentials) ~>
      Route.seal(groupsServiceRoutes.route) ~> check {
      handled shouldBe true
    }
  }

  "Group Prefix Endpoint - GET" should "be handled" in {
    Get("/groups/groupPrefix/members/") ~>
      addCredentials(validCredentials) ~> Route.seal(groupsServiceRoutes.route) ~> check {
      handled shouldBe true
    }
  }

  "Group Prefix / Member Prefix Endpoint - GET" should "be handled" in {
    Get("/groups/groupPrefix/members/cmEmail") ~>
      addCredentials(validCredentials) ~> Route.seal(groupsServiceRoutes.route) ~> check {
      handled shouldBe true
    }
  }

  "Endpoints Beyond Those Set Prefixes - GET" should "not be handled" in {
    Get("/members/groupPrefix/members/cmEmail/nonsense") ~>
      addCredentials(validCredentials) ~> groupsServiceRoutes.route ~> check {
      handled shouldBe false
    }
  }

}
