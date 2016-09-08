package edu.eckerd.google.scgapi.http

import akka.http.scaladsl.marshallers.sprayjson.{SprayJsonSupport => AkkaSprayJsonSupport}
import spray.json.{DefaultJsonProtocol => SprayDefaultJsonProtocol}

/**
  * Created by davenpcm on 9/8/16.
  */
trait JsonProtocol extends AkkaSprayJsonSupport with SprayDefaultJsonProtocol {

}
