package edu.eckerd.google.scgapi.util

import akka.http.scaladsl.marshallers.sprayjson.{SprayJsonSupport => AkkaSprayJsonSupport}
import edu.eckerd.google.scgapi.models._
import spray.json.{JsonFormat, DefaultJsonProtocol => SprayDefaultJsonProtocol}

/**
  * Created by davenpcm on 9/8/16.
  */
trait JsonProtocol extends AkkaSprayJsonSupport with SprayDefaultJsonProtocol {
  implicit val messageJsonProtocol = jsonFormat1(Message)
  implicit val GroupBuilderJsonProtocol = jsonFormat3(GroupBuilder)



}
