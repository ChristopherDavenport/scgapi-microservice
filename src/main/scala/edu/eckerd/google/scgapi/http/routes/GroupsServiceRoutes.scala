package edu.eckerd.google.scgapi.http.routes

import scala.concurrent.ExecutionContext
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.pathPrefix
import akka.http.scaladsl.server.Directives.authenticateBasic
import akka.http.scaladsl.server.Directives.pathEndOrSingleSlash
import akka.http.scaladsl.server.Directives.delete
import akka.http.scaladsl.server.Directives.get
import akka.http.scaladsl.server.Directives.post
import akka.http.scaladsl.server.Directives.entity
import akka.http.scaladsl.server.Directives.as
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.Directives.Segment
import akka.http.scaladsl.server.Directives.rejectEmptyResponse
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import edu.eckerd.google.scgapi.models.GroupBuilder
import edu.eckerd.google.scgapi.models.Message
import edu.eckerd.google.scgapi.services.auth.AuthService
import edu.eckerd.google.scgapi.services.core.groups.GroupsService
import edu.eckerd.google.scgapi.http.util.JsonProtocol


/**
  * Created by davenpcm on 9/9/16.
  */
class GroupsServiceRoutes(groupsService: GroupsService, authService: AuthService)
                         (implicit executionContext: ExecutionContext)
  extends JsonProtocol {

  val route : Route = pathPrefix("groups") { authenticateBasic(realm = "*", authService.authenticate) { userName =>
    pathEndOrSingleSlash {
      get {
        complete(Message(s"$userName Please Ask For a Get Request Against A Single Group"))
      } ~
        post {
          entity(as[GroupBuilder]) { groupBuilder =>
            complete(StatusCodes.Created, groupsService.createGroup(groupBuilder.emailValidate))
          }
        } ~
        delete {
          entity(as[GroupBuilder]) { groupBuilder =>
            complete(groupsService.deleteGroup(groupBuilder.emailValidate)
              .map(_ => HttpResponse(StatusCodes.NoContent)))
          }
        }
    } ~
      pathPrefix(Segment) { emailPrefix =>
        pathEndOrSingleSlash {
          get {
            rejectEmptyResponse(complete(groupsService.getGroupByEmail(emailPrefix)))
          }
        }
      }
  }
  }

}

object GroupsServiceRoutes {
  def apply(groupsService: GroupsService, authService: AuthService)(implicit executionContext: ExecutionContext): GroupsServiceRoutes =
    new GroupsServiceRoutes(groupsService, authService)(executionContext)
}
