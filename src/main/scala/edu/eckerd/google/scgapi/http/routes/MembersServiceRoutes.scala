package edu.eckerd.google.scgapi.http.routes

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import edu.eckerd.google.scgapi.http.util.{ErrorResponseHandling, JsonProtocol}
import edu.eckerd.google.scgapi.models.MemberBuilder
import edu.eckerd.google.scgapi.services.auth.AuthService
import edu.eckerd.google.scgapi.services.core.members.MembersService

import scala.concurrent.ExecutionContext

/**
  * Created by Chris Davenport on 9/12/16.
  */
class MembersServiceRoutes(membersService: MembersService, authService: AuthService)
                          (implicit executionContext: ExecutionContext)
  extends JsonProtocol with ErrorResponseHandling {

  val route =  authenticateBasic(realm = "*", authService.authenticate) { _ =>
    pathPrefix("groups") {
      pathPrefix( Segment ) { groupEmailPrefix =>
        pathPrefix("members") {

          pathEndOrSingleSlash {
            get {
              onSuccess(membersService.getMembers(groupEmailPrefix)){
                toRoute(_){members => complete(members)}
              }
            } ~
              post {
                entity(as[MemberBuilder]){ memberBuilder =>
                  onSuccess(membersService.createMember(groupEmailPrefix, memberBuilder)){
                    toRoute(_)(member => complete(StatusCodes.Created, member))
                  }
                }
              } ~
              delete {
                entity(as[MemberBuilder]){ memberBuilder =>
                  onSuccess(membersService.deleteMember(groupEmailPrefix, memberBuilder.email)){
                    toRoute(_)(_ => complete(HttpResponse(StatusCodes.NoContent)))

                  }
                }
              }
          } ~
            pathPrefix( Segment ) { memberEmailPrefix =>
              pathEndOrSingleSlash {
                get {
                  onSuccess(membersService.getMember(groupEmailPrefix, memberEmailPrefix)){
                    toRoute(_){ member =>  complete(member)}
                  }
                } ~
                  delete {
                    onSuccess(membersService.deleteMember(groupEmailPrefix, memberEmailPrefix)){
                      toRoute(_){_ => complete(HttpResponse(StatusCodes.NoContent))}
                    }
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
