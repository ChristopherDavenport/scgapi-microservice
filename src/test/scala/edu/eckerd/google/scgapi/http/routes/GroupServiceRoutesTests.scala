package edu.eckerd.google.scgapi.http.routes

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

  val groupsService = new GroupsService {


    override def getGroupByEmail(email: String): Future[Option[Group]] = email match {
      case _ if email == completeGroupEmail => Future(Some(completeGroup))


    }
    //  def updateGroup(groupBuilder: GroupBuilder) : Future[Group]
    override def deleteGroup(groupBuilder: GroupBuilder) = ???
    override def createGroup(groupBuilder: GroupBuilder) = ???
  }

  val groupsServiceRoutes = GroupsServiceRoutes(groupsService, authService)

  "route" should "handle GET requests to /groups" in {
    val user = "Chris Davenport"
    val validCredentials = BasicHttpCredentials(user, "password")
    Get(s"/groups/") ~> addCredentials(validCredentials) ~>
      Route.seal(groupsServiceRoutes.route) ~> check {
      responseAs[Message] shouldEqual Message(s"$user Please Ask For a Get Request Against A Single Group")
    }
  }

  it should "handle authenticated GET requests for an extant completeGroup" in {
    val user = "Chris Davenport"
    val validCredentials = BasicHttpCredentials(user, "password")
    Get(s"/groups/$completeGroupPrefix") ~> addCredentials(validCredentials) ~>
      Route.seal(groupsServiceRoutes.route) ~> check {
      responseAs[Group] shouldEqual completeGroup
    }
  }


}
