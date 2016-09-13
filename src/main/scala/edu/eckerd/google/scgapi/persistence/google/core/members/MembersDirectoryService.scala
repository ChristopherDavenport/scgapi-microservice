package edu.eckerd.google.scgapi.persistence.google.core.members

import edu.eckerd.google.scgapi.models.{Member, MemberBuilder, Members}

import scala.concurrent.Future

/**
  * Created by Chris Davenport on 9/13/16.
  */
trait MembersDirectoryService {

  def getMember   (groupEmail: String, memberEmail: String)   : Option[Member]
  def getMembers  (groupEmail: String)                        : Members
  def createMember(groupEmail: String, member: MemberBuilder) : Member
  def deleteMember(groupEmail: String, memberEmail: String)   : Unit

}
