package edu.eckerd.google.scgapi.http.routes

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import edu.eckerd.google.scgapi.http.util.{ErrorResponseHandling, JsonProtocol}
import edu.eckerd.google.scgapi.models.{GroupBuilder, Message}
import edu.eckerd.google.scgapi.services.auth.AuthService
import edu.eckerd.google.scgapi.services.core.groups.GroupsService

import scala.concurrent.ExecutionContext


/**
  * Created by Chris Davenport on 9/9/16.
  */
class GroupsServiceRoutes(groupsService: GroupsService, authService: AuthService)
                         (implicit executionContext: ExecutionContext)
  extends JsonProtocol with ErrorResponseHandling {

  val route : Route = pathPrefix("groups") { authenticateBasic(realm = "*", authService.authenticate) { userName =>
    pathEndOrSingleSlash {
      get {
        complete(Message(s"$userName Please Ask For a Get Request Against A Single Group"))
      } ~
        post {
          entity(as[GroupBuilder]) { groupBuilder =>
            onSuccess(groupsService.createGroup(groupBuilder.emailValidate)){
              toRoute(_)(group => complete(StatusCodes.Created, group))
            }
          }
        } ~
        delete {
          entity(as[GroupBuilder]) { groupBuilder =>
            onSuccess(groupsService.createGroup(groupBuilder.emailValidate)){
              toRoute(_){_ => complete(HttpResponse(StatusCodes.NoContent))}
            }
          }
        }
    } ~
      pathPrefix(Segment) { emailPrefix =>
        pathEndOrSingleSlash {
          get {
            onSuccess(groupsService.getGroupByEmail(emailPrefix)){
             toRoute(_)(group => complete(group))
            }
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
