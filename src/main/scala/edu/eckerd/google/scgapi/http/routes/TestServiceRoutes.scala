package edu.eckerd.google.scgapi.http.routes

import akka.http.scaladsl.server.Directives._
import edu.eckerd.google.scgapi.models.Message
import edu.eckerd.google.scgapi.services.auth.AuthService
import edu.eckerd.google.scgapi.util.JsonProtocol

/**
  * Created by davenpcm on 9/9/16.
  */
class TestServiceRoutes(authService: AuthService) extends JsonProtocol{

  val route = pathPrefix("test"){
    pathEndOrSingleSlash{
      get{
        complete(Message("Server is Running"))
      }
    } ~
      path("secure") {
        authenticateBasicAsync(realm = "*", authService.authenticate) { userName =>
          pathEndOrSingleSlash {
            get {
              complete(Message(s"Secure Get By - $userName"))
            } ~
              post {
                complete(Message(s"Secure Post By - $userName"))
              } ~
              put {
                complete(Message(s"Secure Put By - $userName"))
              } ~
              delete {
                complete(Message(s"Secure Delete By - $userName"))
              }
          }
        }
      }
  }

}
