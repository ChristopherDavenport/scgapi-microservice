package edu.eckerd.google.scgapi.http.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import edu.eckerd.google.scgapi.http.util.JsonProtocol
import edu.eckerd.google.scgapi.models.Message
import edu.eckerd.google.scgapi.services.auth.AuthService
import edu.eckerd.google.scgapi.services.core.members.MembersService

import scala.concurrent.ExecutionContext

/**
  * Created by davenpcm on 9/12/16.
  */
class MembersServiceRoutes(membersService: MembersService, authService: AuthService)
                          (implicit executionContext: ExecutionContext)
  extends JsonProtocol {

  import StatusCodes._

  val route = pathPrefix("members") { authenticateBasic(realm = "*", authService.authenticate) { userName =>
    pathEndOrSingleSlash{
      get{
        complete(Message(s"$userName Please Ask For a Get Request Against A Single Group"))
      }
    } ~
      pathPrefix( Segment ){ groupEmailPrefix =>
        pathEndOrSingleSlash{
          get{
            complete("")
          } ~
            post{
              complete("")
            } ~
            delete{
              complete("")
            }
        } ~
        pathPrefix( Segment ){ memberEmailPrefix =>
          pathEndOrSingleSlash{
            get{
              complete("")
            }
          }
        }
      }
  }
  }

}

object MembersServiceRoutes {
  def apply(membersService: MembersService, authService: AuthService)
           (implicit executionContext: ExecutionContext): MembersServiceRoutes =
    new MembersServiceRoutes(membersService, authService)(executionContext)
}
