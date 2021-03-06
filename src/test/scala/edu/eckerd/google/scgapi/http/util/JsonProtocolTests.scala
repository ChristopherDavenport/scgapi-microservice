package edu.eckerd.google.scgapi.http.util

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import edu.eckerd.google.scgapi.models.MemberRoles.{MANAGER, MEMBER, OWNER}
import edu.eckerd.google.scgapi.models.MemberTypes.{GROUP, USER}
import edu.eckerd.google.scgapi.models._
import org.scalatest.{FlatSpec, Matchers}
import spray.json.{DeserializationException, JsString}

/**
  * Created by Chris Davenport on 9/11/16.
  */
class JsonProtocolTests extends FlatSpec with Matchers with ScalatestRouteTest with JsonProtocol {

  object GroupExamples {
    val adminCreatedTrue = true
    val completeGroupPrefix = "completeGroup"
    val completeGroupEmail = completeGroupPrefix + "@eckerd.edu"
    val completeGroup = CompleteGroup(
      completeGroupEmail,
      "Complete Group",
      "CompleteLongNonsense11231233",
      4L,
      Some("Best Group Ever"),
      adminCreatedTrue
    )

    val matchedGroupPrefix = "matchedGroup"
    val matchedGroupEmail = matchedGroupPrefix + "@eckerd.edu"
    val matchedGroup = MatchedGroup(
      matchedGroupEmail,
      "Matched Group",
      None,
      Some(17L),
      Some("Matched Group Description"),
      Some(true)
    )

    val groupBuilderEmailPrefix = "groupBuilder"
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
        pathPrefix("completeGroup") {
          pathEndOrSingleSlash {
            post {
              entity(as[CompleteGroup]) { completeGroup =>
                complete(completeGroup)
              }
            }
          }
        } ~
        pathPrefix("matchedGroup") {
          pathEndOrSingleSlash {
            post {
              entity(as[MatchedGroup]) { matchedGroup =>
                complete(matchedGroup)
              }
            }
          }
        } ~
        pathPrefix("groupBuilder") {
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
    pathPrefix("completeMember"){
      pathEndOrSingleSlash {
        post {
          entity(as[CompleteMember]) { completeMember =>
            complete(completeMember)
          }
        }
      }
    } ~
    pathPrefix("matchedMember"){
      pathEndOrSingleSlash{
        post{
          entity(as[MatchedMember]){ matchedMember =>
            complete(matchedMember)
          }
        }
      }
    } ~
    pathPrefix("memberBuilder"){
      pathEndOrSingleSlash{
        post{
          entity(as[MemberBuilder]){ memberBuilder =>
            complete(memberBuilder)
          }
        }
      }
    }
  } ~
  pathPrefix("user"){
    pathEndOrSingleSlash{
      post{
        entity(as[User]){ user =>
          complete(user)
        }
      }
    } ~
    pathPrefix("completeUser"){
      pathEndOrSingleSlash{
        post{
          entity(as[CompleteUser]){ completeUser =>
            complete(completeUser)
          }
        }
      }
    } ~
    pathPrefix("matchedUser"){
      pathEndOrSingleSlash{
        post{
          entity(as[MatchedUser]){ matchedUser =>
            complete(matchedUser)
          }
        }
      }
    } ~
    pathPrefix("userBuilder"){
      pathEndOrSingleSlash{
        post{
          entity(as[UserBuilder]){ userBuilder =>
            complete(userBuilder)
          }
        }
      }
    } ~
    pathPrefix("users"){
      pathEndOrSingleSlash{
        post{
          entity(as[Users]){ users =>
            complete(users)

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
    Post("/group/completeGroup", GroupExamples.completeGroup) ~> route ~> check {
      responseAs[CompleteGroup] shouldEqual GroupExamples.completeGroup
    }
  }

  "MatchedGroupJsonProtocol" should "be able to read and write a MatchedGroup" in {
    Post("/group/matchedGroup", GroupExamples.matchedGroup) ~> route ~> check {
      responseAs[MatchedGroup] shouldEqual GroupExamples.matchedGroup
    }
  }

  "GroupBuilderJsonProtocol" should "be able to read and write a GroupBuilder" in {
    Post("/group/groupBuilder", GroupExamples.groupBuilder) ~> route ~> check {
      responseAs[GroupBuilder] shouldEqual GroupExamples.groupBuilder
    }
  }

  "MessageJsonProtocol" should "be able to read and write a Message" in {
    val message = Message("Message")
    Post("/message", message) ~> route ~> check {
      responseAs[Message] shouldEqual message
    }
  }

  "MemberRoleJsonProtocol" should "parse a JsString Uppercase valid Enumeration" in {
    val j = JsString("MEMBER")
    MemberRolesJsonProtocol.read(j) shouldEqual MEMBER
  }

  it should "parse a JsString Lowercase valid Enumeration" in {
    val j = JsString("owner")
    MemberRolesJsonProtocol.read(j) shouldEqual OWNER
  }

  it should "parse a JsString Mixed Case Enumeration" in {
    val j = JsString("ManAgeR")
    MemberRolesJsonProtocol.read(j) shouldEqual MANAGER
  }

  it should "fail if the string is not a valid Enumeration" in {
    val j = JsString("Monkey")
    intercept[DeserializationException]{
      MemberRolesJsonProtocol.read(j)
    }
  }

  it should "fail if it is given another non JsString Type" in {
    val j = messageJsonProtocol.write(Message("Message"))
    intercept[DeserializationException]{
      MemberRolesJsonProtocol.read(j)
    }
  }

  it should "write the Enumerations to their String Values" in {
    MemberRolesJsonProtocol.write(MANAGER) shouldEqual JsString("MANAGER")
    MemberRolesJsonProtocol.write(OWNER) shouldEqual JsString("OWNER")
    MemberRolesJsonProtocol.write(MEMBER) shouldEqual JsString("MEMBER")
  }

  "CompleteMemberJsonProtocol" should "be able to read and write a CompleteMember" in {
    val cm = CompleteMember("email", "id", MEMBER, USER)
    Post("/member/completeMember", cm) ~> route ~> check {
      responseAs[CompleteMember] shouldEqual cm
    }
  }

  "MatchedMemberJsonProtocol" should "be able to read and write a MatchedMember" in {
    val mm = MatchedMember(Some("email"), Some("id"), OWNER, USER)
    Post("/member/matchedMember", mm) ~> route ~> check {
      responseAs[MatchedMember] shouldEqual mm
    }
  }

  "MemberBuilderJsonProtocol" should "be able to read and write a MemberBuilder" in {
    val mb = MemberBuilder("email", MANAGER, USER)
    Post("/member/memberBuilder", mb) ~> route ~> check {
      responseAs[MemberBuilder] shouldEqual mb
    }
  }

  "MemberJsonProtocol" should "be able to read and write a CompleteMember" in {
    val cm = CompleteMember("email", "id", MEMBER, USER)
    Post("/member", cm) ~> route ~> check {
      responseAs[Member] shouldEqual cm
    }
  }

  it should "be able to read and write a MatchedMember" in {
    val mm = MatchedMember(None, Some("id"), OWNER, USER)
    Post("/member", mm) ~> route ~> check {
      responseAs[Member] shouldEqual mm
    }
  }

  it should "be able to read and write a MemberBuilder" in {
    val mb = MemberBuilder("email", MANAGER, GROUP)
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

  object userTestsObjects{
    private[this] val defaultTrue = true

    val completeUser = CompleteUser(
      "firstName", "lastName", "email", "id", "/",
      defaultTrue, defaultTrue, defaultTrue, defaultTrue, defaultTrue, defaultTrue, defaultTrue
    )
    val matchedUser = MatchedUser(
      "firstName", "lastName", "email", None, None, "/",
      Some(defaultTrue), defaultTrue, defaultTrue, defaultTrue, defaultTrue, defaultTrue, defaultTrue
    )
    val userBuilder = UserBuilder("firstName", "lastName", "email", "password", None, None, None)

    val users = Users(List(completeUser, matchedUser, userBuilder))

  }

  "completeUserJsonProtocol" should "be able to read and write a CompleteUser" in {
    Post("/user/completeUser", userTestsObjects.completeUser) ~> route ~> check {
      responseAs[CompleteUser] shouldEqual userTestsObjects.completeUser
    }
  }

  "matchedUserJsonProtocol" should "be able to read and write a MatchedUser" in {
    Post("/user/matchedUser", userTestsObjects.matchedUser) ~> route ~> check {
      responseAs[MatchedUser] shouldEqual userTestsObjects.matchedUser
    }
  }

  "userBuilderJsonProtocol" should "be able to read and write a UserBuilder" in {
    Post("/user/userBuilder", userTestsObjects.userBuilder) ~> route ~> check {
      responseAs[UserBuilder] shouldEqual userTestsObjects.userBuilder
    }
  }

  "userJsonProtocol" should "be able to read and write a CompleteUser" in {
    Post("/user", userTestsObjects.completeUser) ~> route ~> check {
      responseAs[User] shouldEqual userTestsObjects.completeUser
    }
  }

  it should "be able to read and write a MatchedUser" in {
    Post("/user", userTestsObjects.matchedUser) ~> route ~> check {
      responseAs[User] shouldEqual userTestsObjects.matchedUser
    }
  }

  it should "be able to read and write a UserBuilder" in {
    Post("/user", userTestsObjects.userBuilder) ~> route ~> check {
      responseAs[User] shouldEqual userTestsObjects.userBuilder
    }
  }

  it should "fail to parse another object" in {
    intercept[DeserializationException]{
      MemberJsonProtocol.read(JsString("Nonsense String"))
    }
  }

  "usersJsonProtocol" should "be able to read and write a Users" in {
    Post("/user/users", userTestsObjects.users) ~> route ~> check {
      responseAs[Users] shouldEqual userTestsObjects.users
    }
  }

}
