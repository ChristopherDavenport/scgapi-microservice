package edu.eckerd.google.scgapi.services.core.members

import edu.eckerd.google.scgapi.models.Member
import edu.eckerd.google.scgapi.models.MemberBuilder
import edu.eckerd.google.scgapi.models.Members
import edu.eckerd.google.scgapi.persistence.google.core.members.MembersDirectoryService
import edu.eckerd.google.scgapi.services.core.CoreFunctions

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by davenpcm on 9/9/16.
  */
class MembersServiceImpl(membersDirectoryService: MembersDirectoryService)
                        (implicit executionContext: ExecutionContext) extends MembersService with CoreFunctions {


  def getMember   (groupEmail: String, memberEmail: String)   : Future[Option[Member]] = Future{
    membersDirectoryService.getMember(emailParse(groupEmail), emailParse(memberEmail) )
  }

  def getMembers  (groupEmail: String)                        : Future[Members] = Future {
    membersDirectoryService.getMembers(emailParse(groupEmail))
  }

  def createMember(groupEmail: String, member: MemberBuilder) : Future[Member] = Future{
    membersDirectoryService.createMember(emailParse(groupEmail) , member)
  }

  def deleteMember(groupEmail: String, memberEmail: String)   : Future[Unit] = Future {
    membersDirectoryService.deleteMember(emailParse(groupEmail), emailParse(memberEmail))
  }

}

object MembersServiceImpl{
  def apply(membersDirectoryService: MembersDirectoryService)
           (implicit executionContext: ExecutionContext): MembersServiceImpl = {
    new MembersServiceImpl(membersDirectoryService)(executionContext)
  }
}
