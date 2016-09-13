package edu.eckerd.google.scgapi.http.util

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import edu.eckerd.google.scgapi.models.MemberRoles.{MANAGER, MEMBER, OWNER}
import edu.eckerd.google.scgapi.models.MemberTypes.{CUSTOMER, GROUP, USER}
import edu.eckerd.google.scgapi.models._
import spray.json.{DefaultJsonProtocol, DeserializationException, JsString, JsValue, RootJsonFormat}

import scala.util.Try

/**
  * Created by Chris Davenport on 9/8/16.
  */
trait JsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val messageJsonProtocol        = jsonFormat1(Message)

  implicit val groupBuilderJsonProtocol   = jsonFormat3(GroupBuilder)
  implicit val completeGroupJsonProtocol  = jsonFormat6(CompleteGroup)
  implicit val matchedGroupJsonProtocol   = jsonFormat6(MatchedGroup)

  implicit object GroupJsonProtocol extends RootJsonFormat[Group] {
    /**
      * Group Parser Can Return Either a Matched Group or a Complete Group as these are the types that closely
      * integrate with google. The Group Builder will not be parsed or written when writing as a group as it is
      * an explicit call for less information.
      * @param g The Group To Write
      * @return A Json Parsed Group either as a CompleteGroup or a MatchedGroup
      */
    def write(g: Group) = g match {
      case groupBuilder: GroupBuilder   => groupBuilderJsonProtocol.write(groupBuilder)
      case matchedGroup: MatchedGroup   => matchedGroupJsonProtocol.write(matchedGroup)
      case completeGroup: CompleteGroup => completeGroupJsonProtocol.write(completeGroup)
    }

    def read(value: JsValue) = value match {

      case completeGroup if Try(completeGroupJsonProtocol.read(completeGroup)).isSuccess =>
        completeGroupJsonProtocol.read(completeGroup)

      case matchedGroup if Try(matchedGroupJsonProtocol.read(matchedGroup)).isSuccess && (
          matchedGroupJsonProtocol.read(matchedGroup).adminCreated.isDefined ||
          matchedGroupJsonProtocol.read(matchedGroup).count.isDefined        ||
          matchedGroupJsonProtocol.read(matchedGroup).id.isDefined
        ) =>
        matchedGroupJsonProtocol.read(value)

      case groupBuilder if Try(groupBuilderJsonProtocol.read(groupBuilder)).isSuccess =>
        groupBuilderJsonProtocol.read(groupBuilder)

      case _ => throw DeserializationException("Group Expected")
    }
  }

  implicit object MemberRolesJsonProtocol extends RootJsonFormat[MemberRoles]{
    def write(m: MemberRoles): JsValue = m match {
      case MEMBER   => JsString(MEMBER.entryName)
      case OWNER    => JsString(OWNER.entryName)
      case MANAGER  => JsString(MANAGER.entryName)
    }

    def read(value: JsValue): MemberRoles = value match {

      case JsString(string) => MemberRoles.withNameInsensitiveOption(string)
        .getOrElse(throw DeserializationException("Invalid MemberRole"))

      case _                => throw DeserializationException("Expected MemberRole")
    }
  }

  implicit object MemberTypesJsonProtocol extends RootJsonFormat[MemberTypes]{
    def write(m: MemberTypes): JsValue = m match {
      case CUSTOMER => JsString(CUSTOMER.entryName)
      case GROUP    => JsString(GROUP.entryName)
      case USER     => JsString(USER.entryName)
    }

    def read(json: JsValue): MemberTypes = json match {
      case JsString(string) => MemberTypes.withNameInsensitiveOption(string)
          .getOrElse(throw DeserializationException("Invalid MemberType"))
      case _                => throw DeserializationException("Expecting Member")
    }

  }

  implicit val matchedMemberJsonProtocol  = jsonFormat4(MatchedMember)
  implicit val completeMemberJsonProtocol = jsonFormat4(CompleteMember)
  implicit val memberBuilderJsonProtocol  = jsonFormat3(MemberBuilder)


  implicit object MemberJsonProtocol extends RootJsonFormat[Member]{
    def write(m: Member): JsValue = m match {
      case mb : MemberBuilder => memberBuilderJsonProtocol.write(mb)
      case mm : MatchedMember => matchedMemberJsonProtocol.write(mm)
      case cm : CompleteMember => completeMemberJsonProtocol.write(cm)
    }

    def read(json: JsValue): Member = json match {
      case completeMember if Try(completeMemberJsonProtocol.read(completeMember)).isSuccess =>
        completeMemberJsonProtocol.read(completeMember)
      case memberBuilder if Try(memberBuilderJsonProtocol.read(memberBuilder)).isSuccess =>
        memberBuilderJsonProtocol.read(memberBuilder)
      case matchedMember if Try(matchedMemberJsonProtocol.read(matchedMember)).isSuccess =>
        matchedMemberJsonProtocol.read(matchedMember)
      case _ => throw DeserializationException("Expected Member")
    }
  }

  implicit val membersJsonProtocol = jsonFormat1(Members)

}
