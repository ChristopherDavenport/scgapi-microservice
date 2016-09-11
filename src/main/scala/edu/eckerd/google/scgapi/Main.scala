package edu.eckerd.google.scgapi

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import edu.eckerd.google.scgapi.http.{HttpService, HttpServiceImpl}
import edu.eckerd.google.scgapi.services.auth.AuthServiceImpl
import edu.eckerd.google.scgapi.services.core.groups.{GroupsServiceBasicImpl, GroupsServiceImpl}
import edu.eckerd.google.scgapi.http.util.HttpConfig
import edu.eckerd.google.scgapi.persistence.google.DirectoryService
import edu.eckerd.google.scgapi.persistence.google.DirectoryServiceImpl

import scala.concurrent.ExecutionContext
import scala.io.StdIn

/**
  * Created by davenpcm on 9/8/16.
  */
object Main extends App with HttpConfig {
  implicit val actorSystem = ActorSystem("scgapi")
  implicit val executor: ExecutionContext = actorSystem.dispatcher
  implicit val log : LoggingAdapter = Logging(actorSystem, getClass)
  implicit val materializer: ActorMaterializer = ActorMaterializer()



//  val databaseService = new DatabaseServiceImpl
  val directoryService: DirectoryService = DirectoryServiceImpl()
  val groupsService = GroupsServiceBasicImpl(directoryService)
  val authService = AuthServiceImpl(httpAccessPassword)
  val httpService = HttpServiceImpl(groupsService, authService)


  val bindingFuture = Http().bindAndHandle(httpService.routes, httpHost, httpPort)

  log.info(s"Server online at http://$httpHost:$httpPort/ - Press RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ ⇒ actorSystem.terminate()) // and shutdown when done


}
