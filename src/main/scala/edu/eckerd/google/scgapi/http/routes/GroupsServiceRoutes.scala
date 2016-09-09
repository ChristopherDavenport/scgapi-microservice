package edu.eckerd.google.scgapi.http.routes

import scala.concurrent.ExecutionContext
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import edu.eckerd.google.scgapi.models._
import edu.eckerd.google.scgapi.services.auth.AuthService
import edu.eckerd.google.scgapi.services.core.groups.GroupsService
import edu.eckerd.google.scgapi.util.{HttpConfig, JsonProtocol}
/**
  * Created by davenpcm on 9/9/16.
  */
class GroupsServiceRoutes(groupsService: GroupsService)
                         (implicit executionContext: ExecutionContext)
  extends JsonProtocol {
  import StatusCodes._

  val route = pathPrefix("groups") {
    pathEndOrSingleSlash {
      get {
        println("Group Get Called")
        complete(Message("Please Ask For a Get Request Against A Single Group"))
      } ~
        post {
          entity(as[GroupBuilder]){ groupBuilder =>
            println(groupBuilder)
//            groupsService.createGroup(groupBuilder)
            complete(groupBuilder)
          }
        }
    }
  }

}
