package edu.eckerd.google.scgapi.http.routes

import akka.http.scaladsl.model.{HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.model.headers.BasicHttpCredentials
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import edu.eckerd.google.scgapi.services.auth.AuthServiceImpl
import edu.eckerd.google.scgapi.http.util.JsonProtocol
import edu.eckerd.google.scgapi.models._
import edu.eckerd.google.scgapi.services.core.groups.GroupsService
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Future
/**
  * Created by davenpcm on 9/10/16.
  */
class GroupServiceRoutesTests extends FlatSpec with Matchers with ScalatestRouteTest with JsonProtocol  {

  val authService = AuthServiceImpl("password")

  val completeGroupPrefix = "completegroup"
  val completeGroupEmail = completeGroupPrefix + "@eckerd.edu"
  val completeGroup = CompleteGroup(
    completeGroupEmail,
    "Complete Group",
    "CompleteLongNonsense11231233",
    4L,
    Some("Best Group Ever"),
    true
  )

  val matchedGroupPrefix = "matchedgroup"
  val matchedGroupEmail = matchedGroupPrefix + "@eckerd.edu"
  val matchedGroup = MatchedGroup(
    matchedGroupEmail,
    "Matched Group",
    None,
    Some(17L),
    Some("Matched Group Description"),
    Some(true)
  )

  val groupBuilderEmailPrefix = "groupbuilder"
  val groupBuilderEmail = groupBuilderEmailPrefix + "@eckerd.edu"
  val groupBuilder = GroupBuilder(
    groupBuilderEmail,
    "Group Builder Group",
    Some("Group Builder Description")
  )

  val nonGroupEmailPrefix = "nongroup"
  val nonGroupEmail = nonGroupEmailPrefix + "@eckerd.edu"

  class TestError extends Throwable
  val myTestError = new TestError

  val groupsService = new GroupsService {
    override def getGroupByEmail(email: String): Future[Option[Group]] = email match {
      case _ if email == completeGroupPrefix => Future(Some(completeGroup.copy(email=completeGroupEmail)))
      case _ if email == matchedGroupPrefix => Future(Some(matchedGroup.copy(email = matchedGroupEmail)))
      case _ if email == nonGroupEmailPrefix => Future(None)
    }
    //  def updateGroup(groupBuilder: GroupBuilder) : Future[Group]
    override def deleteGroup(groupBuilder: GroupBuilder): Future[Unit] = groupBuilder.email match {
      case fail if fail == nonGroupEmail => Future.failed(myTestError)
      case _ => Future.successful(())
    }

    override def createGroup(groupBuilder: GroupBuilder): Future[Option[Group]] = groupBuilder.email match {
      case fail if fail == nonGroupEmail => Future.failed(myTestError)
      case complete if complete == completeGroupEmail => Future.successful(Some(completeGroup))
      case _ => Future.successful(Some(groupBuilder))
    }
  }

  val groupsServiceRoutes = GroupsServiceRoutes(groupsService, authService)

  "route /group - GET" should "handle an authenticated request" in {
    val user = "Chris Davenport"
    val validCredentials = BasicHttpCredentials(user, "password")
    Get(s"/groups/") ~> addCredentials(validCredentials) ~>
      Route.seal(groupsServiceRoutes.route) ~> check {
      responseAs[Message] shouldEqual Message(s"$user Please Ask For a Get Request Against A Single Group")
    }
  }

  "route /group - POST" should "indicate if it succesfully created the resource" in {
    val gb = groupBuilder
    val validCredentials = BasicHttpCredentials("Chris Davenport", "password")
    Post(s"/groups/", gb) ~> addCredentials(validCredentials) ~>
      Route.seal(groupsServiceRoutes.route) ~> check {
      status === StatusCodes.Created
      responseAs[Group] shouldEqual gb
    }
  }

  it should "be able to return the resource after creation" in {
    val cg = completeGroup
    val gb = cg.asGroupBuilder
    val validCredentials = BasicHttpCredentials("Chris Davenport", "password")
    Post(s"/groups/", gb) ~> addCredentials(validCredentials) ~>
      Route.seal(groupsServiceRoutes.route) ~> check {
      status === StatusCodes.Created
      responseAs[Group] shouldEqual cg
    }

  }

  "route /group - DELETE" should "return No Content After a Delete" in {
    val gb = GroupBuilder("email", "name", Some("desc"))
    val validCredentials = BasicHttpCredentials("Chris Davenport", "password")
    Delete(s"/groups/", gb) ~> addCredentials(validCredentials) ~>
      Route.seal(groupsServiceRoutes.route) ~> check {
        status === StatusCodes.NoContent
    }
  }


  "route /group/:emailPrefix - GET" should "handle authenticated requests for an extant completeGroup" in {
    val user = "Chris Davenport"
    val validCredentials = BasicHttpCredentials(user, "password")
    Get(s"/groups/$completeGroupPrefix") ~> addCredentials(validCredentials) ~>
      Route.seal(groupsServiceRoutes.route) ~> check {
      responseAs[Group] shouldEqual completeGroup
    }
  }

  it should "reject unauthenticated requests" in {
    val user = "Chris Davenport"
    val invalidCredentials = BasicHttpCredentials(user, "notTheRightPassword")
    Get(s"/groups/$matchedGroupPrefix") ~> addCredentials(invalidCredentials) ~>
      Route.seal(groupsServiceRoutes.route) ~> check {
      status === StatusCodes.Unauthorized
      responseAs[String] shouldEqual "The supplied authentication is invalid"
    }
  }

  it should "handle authenticated requests an extant matchedGroup" in {
    val user = "Chris Davenport"
    val validCredentials = BasicHttpCredentials(user, "password")
    Get(s"/groups/$matchedGroupPrefix") ~> addCredentials(validCredentials) ~>
      Route.seal(groupsServiceRoutes.route) ~> check {
      responseAs[Group] shouldEqual matchedGroup
    }
  }

  it should "handle authenticated requests for a non-extant group" in {
    val user = "Chris Davenport"
    val validCredentials = BasicHttpCredentials(user, "password")
    Get(s"/groups/$nonGroupEmailPrefix") ~> addCredentials(validCredentials) ~>
      Route.seal(groupsServiceRoutes.route) ~> check {
      status === StatusCodes.NotFound
      responseAs[String] shouldEqual "The requested resource could not be found."
    }
  }

  "Route /group/:emailPrefix/:any - GET"  should "not handle any requests past the second segment" in {
    val user = "Chris Davenport"
    val validCredentials = BasicHttpCredentials(user, "password")
    Get(s"/groups/something/perfect") ~> addCredentials(validCredentials) ~>
      groupsServiceRoutes.route ~> check {
      handled shouldBe false
//      status === StatusCodes.NotFound
//      responseAs[String] shouldEqual "The requested resource could not be found."
    }
  }


}
