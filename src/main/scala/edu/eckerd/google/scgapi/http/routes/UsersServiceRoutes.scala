package edu.eckerd.google.scgapi.http.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.StatusCodes
import edu.eckerd.google.scgapi.http.util.{ErrorResponseHandling, JsonProtocol}
import edu.eckerd.google.scgapi.models.UserBuilder
import edu.eckerd.google.scgapi.services.auth.AuthService
import edu.eckerd.google.scgapi.services.core.users.UsersService

import scala.concurrent.ExecutionContext

/**
  * Created by davenpcm on 9/14/16.
  */
class UsersServiceRoutes(userService: UsersService, authService: AuthService)
                        (implicit executionContext: ExecutionContext) extends ErrorResponseHandling with JsonProtocol {

  val route : Route = authenticateBasic(realm = "*", authService.authenticate) { userName =>
    pathPrefix("users"){
      pathEndOrSingleSlash{
        get{
          onSuccess(userService.getUsers)(toRoute(_)(users => complete(users)))
        } ~
        post{
          entity(as[UserBuilder]) { userBuilder =>
            onSuccess(userService.createUser(userBuilder))(toRoute(_)(user => complete(StatusCodes.Created, user)))
          }
        }
      } ~
      pathPrefix( Segment ) { userEmailPrefix =>
        pathEndOrSingleSlash {
          onSuccess(userService.getUser(userEmailPrefix))(toRoute(_)(user => complete(user)))
        }
      }
    }
  }

}

object UsersServiceRoutes {
  def apply(usersService: UsersService, authService: AuthService)
           (implicit executionContext: ExecutionContext) : UsersServiceRoutes =
    new UsersServiceRoutes(usersService, authService)(executionContext)
}
