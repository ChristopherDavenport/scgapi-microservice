package edu.eckerd.google.scgapi.http.util

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import edu.eckerd.google.scgapi.models.CompleteGroup
import edu.eckerd.google.scgapi.models.CompleteMember
import edu.eckerd.google.scgapi.models.Group
import edu.eckerd.google.scgapi.models.GroupBuilder
import edu.eckerd.google.scgapi.models.MatchedGroup
import edu.eckerd.google.scgapi.models.MatchedMember
import edu.eckerd.google.scgapi.models.Message
import edu.eckerd.google.scgapi.models.Member
import edu.eckerd.google.scgapi.models.MemberBuilder
import edu.eckerd.google.scgapi.models.Members
import edu.eckerd.google.scgapi.models.MemberRoles
import edu.eckerd.google.scgapi.models.MemberRoles.MANAGER
import edu.eckerd.google.scgapi.models.MemberRoles.MEMBER
import edu.eckerd.google.scgapi.models.MemberRoles.OWNER
import edu.eckerd.google.scgapi.models.MemberTypes
import edu.eckerd.google.scgapi.models.MemberTypes.CUSTOMER
import edu.eckerd.google.scgapi.models.MemberTypes.GROUP
import edu.eckerd.google.scgapi.models.MemberTypes.USER
import spray.json.DeserializationException
import spray.json.JsString
import spray.json.JsValue
import spray.json.RootJsonFormat
import spray.json.DefaultJsonProtocol

import scala.util.Try

/**
  * Created by davenpcm on 9/8/16.
  */
trait JsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val messageJsonProtocol        = jsonFormat1(Message)

  implicit val GroupBuilderJsonProtocol   = jsonFormat3(GroupBuilder)
  implicit val CompleteGroupJsonProtocol  = jsonFormat6(CompleteGroup)
  implicit val MatchedGroupJsonProtocol   = jsonFormat6(MatchedGroup)

  implicit object GroupJsonProtocol extends RootJsonFormat[Group] {
    /**
      * Group Parser Can Return Either a Matched Group or a Complete Group as these are the types that closely
      * integrate with google. The Group Builder will not be parsed or written when writing as a group as it is
      * an explicit call for less information.
      * @param g The Group To Write
      * @return A Json Parsed Group either as a CompleteGroup or a MatchedGroup
      */
    def write(g: Group) = g match {
      case groupBuilder: GroupBuilder   => GroupBuilderJsonProtocol.write(groupBuilder)
      case matchedGroup: MatchedGroup   => MatchedGroupJsonProtocol.write(matchedGroup)
      case completeGroup: CompleteGroup => CompleteGroupJsonProtocol.write(completeGroup)
    }

    def read(value: JsValue) = value match {

      case completeGroup if Try(CompleteGroupJsonProtocol.read(value)).isSuccess =>
        CompleteGroupJsonProtocol.read(value)

      case matchedGroup if Try(MatchedGroupJsonProtocol.read(value)).isSuccess && (
          MatchedGroupJsonProtocol.read(value).adminCreated.isDefined ||
          MatchedGroupJsonProtocol.read(value).count.isDefined        ||
          MatchedGroupJsonProtocol.read(value).id.isDefined
        ) =>
        MatchedGroupJsonProtocol.read(value)

      case groupBuilder if Try(GroupBuilderJsonProtocol.read(value)).isSuccess =>
        GroupBuilderJsonProtocol.read(value)

      case _ => throw DeserializationException("Group Expected")
    }
  }

  implicit object MemberRolesJsonProtocol extends RootJsonFormat[MemberRoles]{
    def write(m: MemberRoles) = m match {
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
    def write(m: MemberTypes) = m match {
      case CUSTOMER => JsString(CUSTOMER.entryName)
      case GROUP    => JsString(GROUP.entryName)
      case USER     => JsString(USER.entryName)
    }

    def read(json: JsValue): MemberTypes = json match {

      case JsString(string) => MemberTypes.withNameInsensitiveOption(string)
          .getOrElse(throw DeserializationException("Invalid MemberType"))

      case _                => throw DeserializationException("Expected MemberType")
    }

  }

  implicit val MatchedMemberJsonProtocol  = jsonFormat4(MatchedMember)
  implicit val CompleteMemberJsonProtocol = jsonFormat4(CompleteMember)
  implicit val MemberBuilderJsonProtocol  = jsonFormat3(MemberBuilder)


  implicit object MemberJsonProtocol extends RootJsonFormat[Member]{
    def write(m: Member) = m match {
      case mb : MemberBuilder => MemberBuilderJsonProtocol.write(mb)
      case mm : MatchedMember => MatchedMemberJsonProtocol.write(mm)
      case cm : CompleteMember => CompleteMemberJsonProtocol.write(cm)
    }

    def read(json: JsValue): Member = json match {
      case completeMember if Try(CompleteMemberJsonProtocol.read(completeMember)).isSuccess =>
        CompleteMemberJsonProtocol.read(completeMember)
      case memberBuilder if Try(MemberBuilderJsonProtocol.read(memberBuilder)).isSuccess =>
        MemberBuilderJsonProtocol.read(memberBuilder)
      case matchedMember if Try(MatchedMemberJsonProtocol.read(matchedMember)).isSuccess =>
        MatchedMemberJsonProtocol.read(matchedMember)
      case _ => throw DeserializationException("Expected Member")
    }
  }

  implicit val MembersJsonProtocol = jsonFormat1(Members)

}
