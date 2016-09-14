package edu.eckerd.google.scgapi.persistence.google.conversions

import edu.eckerd.google.api.services.directory.models.{Member => gMember}
import edu.eckerd.google.scgapi.models._

/**
  * Created by Chris Davenport on 9/13/16.
  */
trait MemberConversions {

  def completeMemberTogMember (cm: CompleteMember)  : gMember =
    gMember(Some(cm.email), Some(cm.id), cm.role.entryName, cm.`type`.entryName)

  def matchedMemberTogMember  (mm: MatchedMember)   : gMember =
    gMember(mm.email, mm.id, mm.role.entryName, mm.`type`.entryName)

  def gMemberToMember         (gm: gMember)         : Member =
    Member(gm.email, gm.id, MemberRoles.withNameInsensitive(gm.role), MemberTypes.withNameInsensitive(gm.memberType))

}
