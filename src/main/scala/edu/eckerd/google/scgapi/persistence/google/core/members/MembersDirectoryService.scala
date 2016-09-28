package edu.eckerd.google.scgapi.persistence.google.core.members

import edu.eckerd.google.scgapi.models.{ErrorResponse, Member, MemberBuilder, Members}

/**
  * Created by Chris Davenport on 9/13/16.
  */
trait MembersDirectoryService {

  def getMember   (groupEmail: String, memberEmail: String)   : Either[ErrorResponse, Member]
  def getMembers  (groupEmail: String)                        : Either[ErrorResponse, Members]
  def createMember(groupEmail: String, member: MemberBuilder) : Either[ErrorResponse, Member]
  def deleteMember(groupEmail: String, memberEmail: String)   : Either[ErrorResponse, Unit]

}
