package edu.eckerd.google.scgapi.http.util

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import edu.eckerd.google.scgapi.models.{CompleteGroup, Group, GroupBuilder, MatchedGroup}
import edu.eckerd.google.scgapi.models.Message
import org.scalatest.{FlatSpec, Matchers}
import spray.json.DeserializationException

/**
  * Created by davenpcm on 9/11/16.
  */
class JsonProtocolTests extends FlatSpec with Matchers with ScalatestRouteTest with JsonProtocol {

  object GroupExamples {
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
  }

  val route =
    pathPrefix("group") {
      pathEndOrSingleSlash {
        post {
          entity(as[Group]) { group =>
            complete(group)
          }
        }
      } ~
        pathPrefix("completegroup") {
          pathEndOrSingleSlash {
            post {
              entity(as[CompleteGroup]) { completeGroup =>
                complete(completeGroup)
              }
            }
          }
        } ~
        pathPrefix("matchedgroup") {
          pathEndOrSingleSlash {
            post {
              entity(as[MatchedGroup]) { matchedGroup =>
                complete(matchedGroup)
              }
            }
          }
        } ~
        pathPrefix("groupbuilder") {
          pathEndOrSingleSlash {
            post {
              entity(as[GroupBuilder]) { groupBuilder =>
                complete(groupBuilder)
              }
            }
          }
        }
    } ~
    pathPrefix("message") {
      pathEndOrSingleSlash {
        post {
          entity(as[Message]) { message =>
            complete(message)
          }
        }
      }
    }

  "GroupJsonProtocol" should "be able to read and write a Complete Group" in {
    Post("/group", GroupExamples.completeGroup) ~> route ~> check {
      responseAs[Group] shouldEqual GroupExamples.completeGroup
    }
  }

  it should "be able to read and write a MatchedGroup" in {
    Post("/group", GroupExamples.matchedGroup) ~> route ~> check {
      responseAs[Group] shouldEqual GroupExamples.matchedGroup
    }
  }

  // Accessory Methods Such as .asGroupBuilder can easily transition this but this gives greatest clarity and
  // most information on return
  it should "be able to read and write a GroupBuilder as a Matched Group" in {
    Post("/group", GroupExamples.groupBuilder) ~> route ~> check {
      responseAs[Group] shouldEqual GroupExamples.groupBuilder
    }
  }

  it should "give a rejection if given something else" in {
    Post("/group", Message("Test Fail Message")) ~> route ~> check {
        rejection
    }
  }

  it should "give a malformed rejection message when given something else" in {
    Post("/group", Message("Test Fail Message")) ~> Route.seal(route) ~> check {
      status === StatusCodes.BadRequest
      responseAs[String] shouldEqual "The request content was malformed:\nGroup Expected"
    }
  }

  "CompleteGroupJsonProtocol" should "be able to read and write a CompleteGroup" in {
    Post("/group/completegroup", GroupExamples.completeGroup) ~> route ~> check {
      responseAs[CompleteGroup] shouldEqual GroupExamples.completeGroup
    }
  }

  "MatchedGroupJsonProtocol" should "be able to read and write a MatchedGroup" in {
    Post("/group/matchedgroup", GroupExamples.matchedGroup) ~> route ~> check {
      responseAs[MatchedGroup] shouldEqual GroupExamples.matchedGroup
    }
  }

  "GroupBuilderJsonProtocol" should "be able to read and write a GroupBuilder" in {
    Post("/group/groupbuilder", GroupExamples.groupBuilder) ~> route ~> check {
      responseAs[GroupBuilder] shouldEqual GroupExamples.groupBuilder
    }
  }

  "MessageJsonProtocol" should "be able to read and write a Message" in {
    val message = Message("Message")
    Post("/message", message) ~> route ~> check {
      responseAs[Message] shouldEqual message
    }
  }




}
