package edu.eckerd.google.scgapi.http.routes

import scala.concurrent.ExecutionContext
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import edu.eckerd.google.scgapi.models._
import edu.eckerd.google.scgapi.services.auth.AuthService
import edu.eckerd.google.scgapi.services.core.groups.GroupsService
import edu.eckerd.google.scgapi.http.util.{HttpConfig, JsonProtocol}
import scala.concurrent.Future
/**
  * Created by davenpcm on 9/9/16.
  */
class GroupsServiceRoutes(groupsService: GroupsService, authService: AuthService)
                         (implicit executionContext: ExecutionContext)
  extends JsonProtocol {
  import StatusCodes._

  val route = pathPrefix("groups") {
    pathEndOrSingleSlash {
      get {
        authenticateBasic(realm = "*", authService.authenticate) { userName =>
          complete(Message(s"$userName Please Ask For a Get Request Against A Single Group"))
        }
      } ~
        post {
          authenticateBasic(realm = "*", authService.authenticate) { userName =>
            entity(as[GroupBuilder]) { groupBuilder =>
              println(s"$userName posted $groupBuilder to groups")
              complete(groupBuilder)
            }
          }
        } ~
      put {
        authenticateBasic(realm = "*", authService.authenticate) { userName =>
          val msg = s"$userName called groups Put - Not Implemented"
          println(msg)
          complete(Message(msg))

        }
      } ~
        delete {
          authenticateBasic(realm = "*", authService.authenticate) { userName =>
            entity(as[GroupBuilder]) { groupBuilder =>
              println(s"$userName called groups Delete - $groupBuilder")
              complete(groupBuilder)
            }
          }
        }


    } ~
    pathPrefix( Segment ){ emailPrefix =>
      pathEndOrSingleSlash{
        get{
          authenticateBasic(realm = "*", authService.authenticate) { userName =>
            println(s"$userName called groups GET for group - $emailPrefix@eckerd.edu")
              complete(groupsService.getGroupByEmail(emailPrefix + "@eckerd.edu"))
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
