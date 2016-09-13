package edu.eckerd.google.scgapi.services.core.members

import edu.eckerd.google.scgapi.models.Member
import edu.eckerd.google.scgapi.models.MemberBuilder
import edu.eckerd.google.scgapi.models.Members
import scala.concurrent.Future
/**
  * Created by Chris Davenport on 9/9/16.
  */
trait MembersService {

  def getMember   (groupEmail: String, memberEmail: String)   : Future[Option[Member]]
  def getMembers  (groupEmail: String)                        : Future[Members]
  def createMember(groupEmail: String, member: MemberBuilder) : Future[Member]
  def deleteMember(groupEmail: String, memberEmail: String)   : Future[Unit]

}
