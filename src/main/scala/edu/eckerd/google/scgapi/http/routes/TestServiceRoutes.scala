package edu.eckerd.google.scgapi.http.routes

import akka.http.scaladsl.server.Directives._
import edu.eckerd.google.scgapi.models.Message
import edu.eckerd.google.scgapi.services.auth.AuthService
import edu.eckerd.google.scgapi.http.util.JsonProtocol

/**
  * Created by davenpcm on 9/9/16.
  */
class TestServiceRoutes(authService: AuthService) extends JsonProtocol{

  val route = pathPrefix("test"){
    pathEndOrSingleSlash{
      get{
        val msg = "Insecure Get"
        complete(Message(msg))
      } ~
      post{
        val msg = "Insecure Post"
        complete(Message(msg))
      } ~
      put {
        val msg = "Insecure Put"
        complete(Message(msg))
      } ~
      delete {
        val msg = "Insecure Delete"
        complete(Message(msg))
      }
    } ~
      path("secure") {
          pathEndOrSingleSlash {
            get {
              authenticateBasic(realm = "*", authService.authenticate){ userName =>
                val msg = s"Secure Get By - $userName"
                complete(Message(msg))
              }
            } ~
              post {
                authenticateBasic(realm = "*", authService.authenticate) { userName =>
                  val msg = s"Secure Post By - $userName"
                  complete(Message(msg))
                }
              } ~
              put {
                authenticateBasic(realm = "*", authService.authenticate) { userName =>
                  complete(Message(s"Secure Put By - $userName"))
                }
              } ~
              delete {
                authenticateBasic(realm = "*", authService.authenticate) { userName =>
                  complete(Message(s"Secure Delete By - $userName"))
                }
              }
          }
      }
  }

}

object TestServiceRoutes {
  def apply(authService: AuthService): TestServiceRoutes = new TestServiceRoutes(authService)
}
