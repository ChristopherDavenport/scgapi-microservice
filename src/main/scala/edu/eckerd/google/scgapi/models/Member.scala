package edu.eckerd.google.scgapi.models

/**
  * Created by davenpcm on 9/8/16.
  */
sealed trait Member{
  def validateEmail: Member
}

object Member {
  def apply(emailOpt: Option[String], idOpt: Option[String], role: MemberRoles, `type`: MemberTypes): Member = {
    (emailOpt, idOpt, role, `type`) match {

      case (Some(email), Some(id), r, t)  => CompleteMember(email, id, r, t )
      case (Some(email), None, r, t)      => MemberBuilder(email, r, t)
      case (None, idOption, r, t)         => MatchedMember(None, idOption, r, t)
    }
  }

}

final case class CompleteMember(
                                 email: String,
                                 id: String,
                                 role: MemberRoles,
                                 `type`: MemberTypes
                               ) extends Member {
  def validateEmail: CompleteMember = if (email.contains('@')) this else this.copy(email = email + "@eckerd.edu")
}

final case class MemberBuilder(
                                email: String,
                                role: MemberRoles,
                                `type`: MemberTypes
                              ) extends Member {
  def validateEmail: MemberBuilder = if (email.contains('@')) this else this.copy(email = email + "@eckerd.edu")
}

final case class MatchedMember(
                                email: Option[String],
                                id: Option[String],
                                role: MemberRoles,
                                `type`: MemberTypes
                              ) extends Member {
  def validateEmail: MatchedMember = email.map{ e =>
    if ( !e.contains('@') ){
      this.copy(email = Some(e + "@eckerd.edu") )
    } else {
      this
    }
  }.getOrElse(this)
}




