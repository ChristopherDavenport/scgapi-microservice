package edu.eckerd.google.scgapi.http.util

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import edu.eckerd.google.scgapi.models.{CompleteGroup, Group, GroupBuilder, MatchedGroup}
import edu.eckerd.google.scgapi.models.{CompleteMember, MatchedMember, Member, MemberBuilder}
import edu.eckerd.google.scgapi.models.MemberRole.{MANAGER, MEMBER, OWNER}
import edu.eckerd.google.scgapi.models.Message
import org.scalatest.{FlatSpec, Matchers}
import spray.json.{DeserializationException, JsString}

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
    } ~
  pathPrefix("member"){
    pathEndOrSingleSlash{
      post {
        entity(as[Member]){ member =>
            complete(member)
        }
      }
    } ~
    pathPrefix("completemember"){
      pathEndOrSingleSlash {
        post {
          entity(as[CompleteMember]) { completeMember =>
            complete(completeMember)
          }
        }
      }
    } ~
    pathPrefix("matchedmember"){
      pathEndOrSingleSlash{
        post{
          entity(as[MatchedMember]){ matchedmember =>
            complete(matchedmember)
          }
        }
      }
    } ~
    pathPrefix("memberbuilder"){
      pathEndOrSingleSlash{
        post{
          entity(as[MemberBuilder]){ memberbuilder =>
            complete(memberbuilder)
          }
        }
      }
    }
  }

  "GroupJsonProtocol" should "be able to read and write a Complete Group" in {
    Post("/group", GroupExamples.completeGroup) ~> route ~> check {
      responseAs[Group] shouldEqual GroupExamples.completeGroup
    }
  }

  it should "fail to parse a Non-Group JsonObject" in {
    intercept[DeserializationException]{
      GroupJsonProtocol.read(JsString("Yellow"))
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

  "MemberRoleJsonProtocol" should "parse a Jstring Uppercase valid Enumeration" in {
    val j = JsString("MEMBER")
    MemberRoleJsonProtocol.read(j) shouldEqual MEMBER
  }

  it should "parse a Jstring Lowercase valid Enumeration" in {
    val j = JsString("owner")
    MemberRoleJsonProtocol.read(j) shouldEqual OWNER
  }

  it should "parse a Jstring Mixed Case Enumeration" in {
    val j = JsString("ManAgeR")
    MemberRoleJsonProtocol.read(j) shouldEqual MANAGER
  }

  it should "fail if the string is not a valid Enumeration" in {
    val j = JsString("Monkey")
    intercept[DeserializationException]{
      MemberRoleJsonProtocol.read(j)
    }
  }

  it should "write the Enumerations to their String Values" in {
    MemberRoleJsonProtocol.write(MANAGER) shouldEqual JsString("MANAGER")
    MemberRoleJsonProtocol.write(OWNER) shouldEqual JsString("OWNER")
    MemberRoleJsonProtocol.write(MEMBER) shouldEqual JsString("MEMBER")
  }

  "CompleteMemberJsonProtocol" should "be able to read and write a CompleteMember" in {
    val cm = CompleteMember("email", "id", MEMBER, "USER")
    Post("/member/completemember", cm) ~> route ~> check {
      responseAs[CompleteMember] shouldEqual cm
    }
  }

  "MatchedMemberJsonProtocol" should "be able to read and write a MatchedMember" in {
    val mm = MatchedMember(Some("email"), Some("id"), OWNER, "USER")
    Post("/member/matchedmember", mm) ~> route ~> check {
      responseAs[MatchedMember] shouldEqual mm
    }
  }

  "MemberBuilderJsonProtocol" should "be able to read and write a MemberBuilder" in {
    val mb = MemberBuilder("email", Some(MANAGER))
    Post("/member/memberbuilder", mb) ~> route ~> check {
      responseAs[MemberBuilder] shouldEqual mb
    }
  }

  "MemberJsonProtocol" should "be able to read and write a CompleteMember" in {
    val cm = CompleteMember("email", "id", MEMBER, "USER")
    Post("/member", cm) ~> route ~> check {
      responseAs[Member] shouldEqual cm
    }
  }

  it should "be able to read and write a MatchedMember" in {
    val mm = MatchedMember(Some("email"), None, OWNER, "USER")
    Post("/member", mm) ~> route ~> check {
      responseAs[Member] shouldEqual mm
    }
  }

  it should "be able to read and write a MemberBuilder" in {
    val mb = MemberBuilder("email", Some(MANAGER))
    Post("/member", mb) ~> route ~> check {
      responseAs[Member] shouldEqual mb
    }
  }

  it should "throw a deserialization error when given bad data" in {
    Post("/member", Message("Test Fail Message")) ~> route ~> check {
      rejection
    }
  }

  it should "fail when given a JsString" in {
    intercept[DeserializationException]{
      MemberJsonProtocol.read(JsString("Bad String!"))
    }
  }






}
