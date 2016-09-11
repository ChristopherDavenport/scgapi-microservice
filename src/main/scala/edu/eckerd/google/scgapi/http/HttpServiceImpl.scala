package edu.eckerd.google.scgapi.http

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import edu.eckerd.google.scgapi.http.routes.{GroupsServiceRoutes, TestServiceRoutes}
import edu.eckerd.google.scgapi.services.auth.AuthService
import edu.eckerd.google.scgapi.services.core.groups.GroupsService
import edu.eckerd.google.scgapi.http.util.{CorsSupport, HttpConfig}


import scala.concurrent.ExecutionContext
/**
  * Created by davenpcm on 9/8/16.
  */
class HttpServiceImpl(groupsService: GroupsService, authService: AuthService)
                     (implicit executionContext: ExecutionContext) extends HttpService with  CorsSupport {

  val testRouter = new TestServiceRoutes(authService)
  val groupRouter = new GroupsServiceRoutes(groupsService, authService)

  val routes : Route = corsHandler{
      groupRouter.route ~ testRouter.route
  }

}

object HttpServiceImpl {
  def apply(groupsService: GroupsService, authService: AuthService)(implicit executionContext: ExecutionContext): HttpServiceImpl =
    new HttpServiceImpl(groupsService, authService)(executionContext)
}

