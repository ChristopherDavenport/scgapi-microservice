package edu.eckerd.google.scgapi.services.core.members

import edu.eckerd.google.scgapi.models.{ErrorResponse, Member, MemberBuilder, Members}

import scala.concurrent.Future
/**
  * Created by Chris Davenport on 9/9/16.
  */
trait MembersService {

  def getMember   (groupEmail: String, memberEmail: String)   : Future[Either[ErrorResponse, Member ]]
  def getMembers  (groupEmail: String)                        : Future[Either[ErrorResponse, Members]]
  def createMember(groupEmail: String, member: MemberBuilder) : Future[Either[ErrorResponse, Member ]]
  def deleteMember(groupEmail: String, memberEmail: String)   : Future[Either[ErrorResponse, Unit   ]]

}
