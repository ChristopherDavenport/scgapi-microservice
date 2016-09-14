package edu.eckerd.google.scgapi.models

/**
  * Created by Chris Davenport on 9/8/16.
  */
sealed trait Group{
  val email: String
  val name: String
  val desc: Option[String]

  def asGroupBuilder: GroupBuilder = GroupBuilder(
    email,
    name,
    desc
  )

  def asMatchedGroup: MatchedGroup

}

object Group{
  def apply(
             email: String,
             name: String,
             desc: Option[String]
           ): GroupBuilder = GroupBuilder(email, name,desc)

  def apply(
             email: String,
             name: String,
             id: String,
             count: Long,
             desc: Option[String],
             adminCreated: Boolean
           ): CompleteGroup =
    CompleteGroup(email, name, id, count, desc, adminCreated)

  def apply(
             email:String,
             name: String,
             id: Option[String],
             count: Option[Long],
             desc: Option[String],
             adminCreated: Option[Boolean]
           ): Group = {
    for{
      pId <- id
      pCount <- count
      pAdminCreated <- adminCreated
    } yield CompleteGroup(email, name, pId, pCount, desc, pAdminCreated)
  }
    .getOrElse {
      if (id.isDefined || count.isDefined || adminCreated.isDefined) {
        MatchedGroup(email, name, id, count, desc, adminCreated)
      } else {
        GroupBuilder(email, name, desc)
      }
    }


}

final case class CompleteGroup(
                          email: String,
                          name: String,
                          id: String,
                          count: Long,
                          desc: Option[String],
                          adminCreated: Boolean
                        ) extends Group {

  def asMatchedGroup: MatchedGroup = MatchedGroup(
    email,
    name,
    Some(id),
    Some(count),
    desc,
    Some(adminCreated)
  )

}

final case class MatchedGroup(
                         email: String,
                         name: String,
                         id: Option[String],
                         count: Option[Long],
                         desc: Option[String],
                         adminCreated: Option[Boolean]
                       ) extends Group{
  def asMatchedGroup: MatchedGroup = this
}

final case class GroupBuilder(
                         email: String,
                         name: String,
                         desc: Option[String]
                       ) extends Group {
  def emailValidate = if (!email.contains('@')) GroupBuilder(email + "@eckerd.edu", name, desc) else this
  def asMatchedGroup: MatchedGroup = MatchedGroup(email, name, None, None, desc, None)
}

