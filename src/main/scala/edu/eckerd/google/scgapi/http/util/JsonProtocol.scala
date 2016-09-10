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
    def write(g: Group) = g match {
      case groupBuilder: GroupBuilder   => GroupBuilderJsonProtocol.write(groupBuilder)
      case matchedGroup: MatchedGroup   => MatchedGroupJsonProtocol.write(matchedGroup)
      case completeGroup: CompleteGroup => CompleteGroupJsonProtocol.write(completeGroup)
    }

    def read(value: JsValue) = value match {
      case completeGroup if Try(CompleteGroupJsonProtocol.read(value)).isSuccess =>
        CompleteGroupJsonProtocol.read(value)
      case matchedGroup if Try(MatchedGroupJsonProtocol.read(value)).isSuccess =>
        MatchedGroupJsonProtocol.read(value)
      case groupBuilder if Try(GroupBuilderJsonProtocol.read(value)).isSuccess =>
        GroupBuilderJsonProtocol.read(value)
      case _ => throw DeserializationException("Group Expected")
    }
  }

}
