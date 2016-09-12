package edu.eckerd.google.scgapi.http.util

import akka.http.scaladsl.marshallers.sprayjson.{SprayJsonSupport => AkkaSprayJsonSupport}
import edu.eckerd.google.scgapi.models._
import spray.json.{JsValue, JsonFormat, RootJsonFormat, DefaultJsonProtocol => SprayDefaultJsonProtocol, DeserializationException}

import scala.util.Try

/**
  * Created by davenpcm on 9/8/16.
  */
trait JsonProtocol extends AkkaSprayJsonSupport with SprayDefaultJsonProtocol {

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

}
