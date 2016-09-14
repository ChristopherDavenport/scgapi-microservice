package edu.eckerd.google.scgapi.http

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import edu.eckerd.google.scgapi.http.routes.{GroupsServiceRoutes, MembersServiceRoutes, TestServiceRoutes, UsersServiceRoutes}
import edu.eckerd.google.scgapi.http.util.CorsSupport
import edu.eckerd.google.scgapi.services.auth.AuthService
import edu.eckerd.google.scgapi.services.core.groups.GroupsService
import edu.eckerd.google.scgapi.services.core.members.MembersService
import edu.eckerd.google.scgapi.services.core.users.UsersService

import scala.concurrent.ExecutionContext
/**
  * Created by Chris Davenport on 9/8/16.
  */
class HttpServiceImpl(
                       groupsService: GroupsService,
                       membersService: MembersService,
                       usersService: UsersService,
                       authService: AuthService
                     )
                     (implicit executionContext: ExecutionContext) extends HttpService with  CorsSupport {

  val testRouter = TestServiceRoutes(authService)
  val groupsRouter = GroupsServiceRoutes(groupsService, authService)
  val membersRouter = MembersServiceRoutes(membersService, authService)
  val usersRouter = UsersServiceRoutes(usersService, authService)

  val routes : Route = corsHandler{
      groupsRouter.route ~ membersRouter.route ~ usersRouter.route ~  testRouter.route
  }

}

object HttpServiceImpl {
  def apply(
             groupsService: GroupsService,
             membersService: MembersService,
             usersService: UsersService,
             authService: AuthService
           )
           (implicit executionContext: ExecutionContext): HttpServiceImpl =
    new HttpServiceImpl(groupsService, membersService, usersService, authService)(executionContext)
}

