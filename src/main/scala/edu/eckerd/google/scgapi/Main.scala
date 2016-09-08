package edu.eckerd.google.scgapi

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContext

/**
  * Created by davenpcm on 9/8/16.
  */
object Main extends App {
  implicit val actorSystem = ActorSystem()
  implicit val executor: ExecutionContext = actorSystem.dispatcher
  implicit val log : LoggingAdapter = Logging(actorSystem, getClass)
  implicit val materializer: ActorMaterializer = ActorMaterializer()



}
