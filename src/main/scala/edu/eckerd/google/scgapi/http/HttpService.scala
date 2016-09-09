package edu.eckerd.google.scgapi.http

import akka.http.scaladsl.server.Directives._
import edu.eckerd.google.scgapi.http.routes.{GroupsServiceRoutes, TestServiceRoutes}
import edu.eckerd.google.scgapi.services.auth.AuthService
import edu.eckerd.google.scgapi.services.core.groups.GroupsService
import edu.eckerd.google.scgapi.util.{CorsSupport, HttpConfig}


import scala.concurrent.ExecutionContext
/**
  * Created by davenpcm on 9/8/16.
  */
class HttpService(groupsService: GroupsService, authService: AuthService)(implicit executionContext: ExecutionContext) extends CorsSupport {

  val testRouter = new TestServiceRoutes(authService)
  val groupRouter = new GroupsServiceRoutes(groupsService)

  val routes = corsHandler{
    authenticateBasicAsync(realm = "*", authService.authenticate) { userName =>
      groupRouter.route
    } ~
    testRouter.route
  }



}
