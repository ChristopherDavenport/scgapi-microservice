package edu.eckerd.google.scgapi

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import edu.eckerd.google.api.services.directory.Directory
import edu.eckerd.google.scgapi.http.HttpService
import edu.eckerd.google.scgapi.persistence.DatabaseServiceImpl
import edu.eckerd.google.scgapi.services.auth.AuthServiceImpl
import edu.eckerd.google.scgapi.services.core.groups.GroupsServiceImpl
import edu.eckerd.google.scgapi.util.HttpConfig

import scala.concurrent.ExecutionContext

/**
  * Created by davenpcm on 9/8/16.
  */
object Main extends App with HttpConfig {
  implicit val actorSystem = ActorSystem()
  implicit val executor: ExecutionContext = actorSystem.dispatcher
  implicit val log : LoggingAdapter = Logging(actorSystem, getClass)
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val adminDir = Directory()

  val databaseService = new DatabaseServiceImpl
  val groupsService = new GroupsServiceImpl(databaseService, adminDir)
  val authService = new AuthServiceImpl(httpAccessPassword)

  val httpService = new HttpService(groupsService, authService)

  Http().bindAndHandle(httpService.routes, httpHost, httpPort)




}
