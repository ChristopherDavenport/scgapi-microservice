package edu.eckerd.google.scgapi.persistence.google.core.members

import edu.eckerd.google.api.services.directory.Directory
import edu.eckerd.google.scgapi.models._
import edu.eckerd.google.scgapi.persistence.google.core.shared.GoogleErrorHandling


/**
  * Created by Chris Davenport on 9/13/16.
  */
trait MembersDirectoryServiceImpl extends MembersDirectoryService with MemberConversions with GoogleErrorHandling {
  val directory: Directory

  def getMember   (groupEmail: String, memberEmail: String)   : Either[ErrorResponse, Member]  = handleGoogleErrors{

    val members = directory.members.list(groupEmail).map(gMemberToMember)

    val member = members
      .collect { case cm: CompleteMember => cm}
      .find(_.email == memberEmail)
      .getOrElse {
      // We throw a custom 404 Missing Error To Be Caught If the Member Doesn't Exist
      throw new Throwable("404 - Missing")
    }

    member
  }

  def getMembers  (groupEmail: String)                        : Either[ErrorResponse, Members]  = handleGoogleErrors{
    val members = directory.members.list(groupEmail).map(gMemberToMember)
    Members(members)
  }

  def createMember(groupEmail: String, member: MemberBuilder) : Either[ErrorResponse, Member]   = handleGoogleErrors{
    val created = directory.members.add(groupEmail, member.email)
    gMemberToMember(created)
  }

  def deleteMember(groupEmail: String, memberEmail: String)   : Either[ErrorResponse, Unit]     = handleGoogleErrors{
    directory.members.remove(groupEmail, memberEmail)
  }

}
