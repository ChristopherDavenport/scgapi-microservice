package edu.eckerd.google.scgapi.persistence.google.core.members

import edu.eckerd.google.api.services.directory.Directory
import edu.eckerd.google.scgapi.models.{CompleteMember, Member, MemberBuilder, Members}
import edu.eckerd.google.scgapi.persistence.google.conversions.MemberConversions

import scala.util.Try

/**
  * Created by davenpcm on 9/13/16.
  */
trait MembersDirectoryServiceImpl extends MembersDirectoryService with MemberConversions {
  val directory: Directory

  def getMember   (groupEmail: String, memberEmail: String)   : Option[Member]  = {
    val tryMembers = Try(directory.members.list(groupEmail))
    println(tryMembers)
    tryMembers.toOption.flatMap{_.map(gMemberToMember)
      .collect{ case cm: CompleteMember => cm }
      .find(_.email == memberEmail)
    }
  }

  def getMembers  (groupEmail: String)                        : Members         = {
    val members = directory.members.list(groupEmail).map(gMemberToMember)
    Members(members)
  }

  def createMember(groupEmail: String, member: MemberBuilder) : Member          = {
    val created = directory.members.add(groupEmail, member.email)
    println(created)
    gMemberToMember(created)
  }

  def deleteMember(groupEmail: String, memberEmail: String)   : Unit            = {
    directory.members.remove(groupEmail, memberEmail)
  }

}
