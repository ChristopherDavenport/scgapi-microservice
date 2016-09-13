package edu.eckerd.google.scgapi.http.routes

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import edu.eckerd.google.scgapi.http.util.JsonProtocol
import edu.eckerd.google.scgapi.models.MemberBuilder
import edu.eckerd.google.scgapi.services.auth.AuthService
import edu.eckerd.google.scgapi.services.core.members.MembersService

import scala.concurrent.ExecutionContext

/**
  * Created by Chris Davenport on 9/12/16.
  */
class MembersServiceRoutes(membersService: MembersService, authService: AuthService)
                          (implicit executionContext: ExecutionContext)
  extends JsonProtocol {

  val route =  authenticateBasic(realm = "*", authService.authenticate) { _ =>
    pathPrefix("groups") {
      pathPrefix( Segment ) { groupEmailPrefix =>
        pathPrefix("members") {

          pathEndOrSingleSlash {
            get {
              complete(membersService.getMembers(groupEmailPrefix))
            } ~
              post {
                entity(as[MemberBuilder]){ memberBuilder =>
                  complete(StatusCodes.Created, membersService.createMember(groupEmailPrefix, memberBuilder))
                }
              } ~
              delete {
                entity(as[MemberBuilder]){ memberBuilder =>
                  complete(membersService.deleteMember(groupEmailPrefix, memberBuilder.email)
                    .map{ _ => HttpResponse(StatusCodes.NoContent)})
                }
              }
          } ~
            pathPrefix( Segment ) { memberEmailPrefix =>
              pathEndOrSingleSlash {
                get {
                  rejectEmptyResponse(complete(membersService.getMember(groupEmailPrefix, memberEmailPrefix)))
                } ~
                  delete {
                    complete(membersService.deleteMember(groupEmailPrefix, memberEmailPrefix)
                      .map{ _ => HttpResponse(StatusCodes.NoContent)})
                  }
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
