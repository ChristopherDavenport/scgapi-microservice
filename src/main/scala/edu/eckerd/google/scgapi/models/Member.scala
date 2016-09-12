package edu.eckerd.google.scgapi.models

/**
  * Created by davenpcm on 9/8/16.
  */
sealed trait Member

object Member {
  def apply(emailOpt: Option[String], idOpt: Option[String], role: String, `type`: String): Member = {
    for {
      email <- emailOpt
      id <- idOpt
    } yield CompleteMember(email, id, MemberRole.namesToValuesMap(role), `type`)
  }.getOrElse( MatchedMember(emailOpt, idOpt, MemberRole.namesToValuesMap(role), `type`) )
  def apply(email: String, role: Option[String]): Member = MemberBuilder(email, role.map(MemberRole.namesToValuesMap(_)))
}

final case class CompleteMember(
                               email: String,
                               id: String,
                               MemberRole: MemberRole,
                               `type`: String
                               ) extends Member

final case class MatchedMember(
                              email: Option[String],
                              id: Option[String],
                              MemberRole: MemberRole,
                              `type`: String
                              ) extends Member

final case class MemberBuilder(
                              email: String,
                              MemberRole: Option[MemberRole]
                              ) extends Member


