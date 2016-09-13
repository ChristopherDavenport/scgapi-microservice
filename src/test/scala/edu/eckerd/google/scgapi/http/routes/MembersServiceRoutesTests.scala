package edu.eckerd.google.scgapi.http.routes

import akka.http.scaladsl.model.headers.BasicHttpCredentials
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import edu.eckerd.google.scgapi.http.util.JsonProtocol
import edu.eckerd.google.scgapi.models.MemberRoles.{MEMBER, OWNER, MANAGER}
import edu.eckerd.google.scgapi.models.MemberTypes.USER
import edu.eckerd.google.scgapi.models._
import edu.eckerd.google.scgapi.services.auth.AuthServiceImpl
import edu.eckerd.google.scgapi.services.core.members.MembersService
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import scala.concurrent.Future

/**
  * Created by davenpcm on 9/12/16.
  */
class MembersServiceRoutesTests extends FlatSpec with Matchers with ScalatestRouteTest with JsonProtocol {

  val completeMember = CompleteMember("cmemail", "id", OWNER, USER)
  val matchedMember  = MatchedMember(None, Some("mmid"), MEMBER, USER)
  val memberBuilder = MemberBuilder("mbemail", MANAGER, USER)


  class TestMemberService extends MembersService{
    def getMember   (groupEmail: String, memberEmail: String)   : Future[Option[Member]] = memberEmail match {
      case complete if complete == completeMember.email => Future.successful(Some(completeMember.validateEmail))
      case mb if mb == memberBuilder.email => Future.successful(Some(memberBuilder.validateEmail))
      case none  if none == "none" => Future.successful(None)
    }

    def getMembers  (groupEmail: String)                        : Future[Members] =
      Future.successful(Members(List(completeMember, matchedMember, memberBuilder)))

    def createMember(groupEmail: String, member: MemberBuilder) : Future[Member] = ???

    def deleteMember(groupEmail: String, memberEmail: String)   : Future[Unit] = ???
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
    Get("/groups/groupPrefix/members/cmemail") ~>
      addCredentials(validCredentials) ~> Route.seal(groupsServiceRoutes.route) ~> check {
      handled shouldBe true
    }
  }

  "Endpoints Beyond Those Set Prefixes - GET" should "not be handled" in {
    Get("/members/groupPrefix/members/cmemail/nonsense") ~>
      addCredentials(validCredentials) ~> groupsServiceRoutes.route ~> check {
      handled shouldBe false
    }
  }

}
