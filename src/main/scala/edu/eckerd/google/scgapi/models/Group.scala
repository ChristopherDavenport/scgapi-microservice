package edu.eckerd.google.scgapi.models

import edu.eckerd.google.api.services.directory.models.{Group => GGroup}

/**
  * Created by davenpcm on 9/8/16.
  */
sealed trait Group

object Group {
  def apply(
             email: String,
             name: String,
             id: String,
             count: Long,
             desc: Option[String],
             adminCreated: Boolean
           ): CompleteGroup = CompleteGroup(email, name, id, count, desc, adminCreated)

  implicit def fromGoogleApiScalaGroup(ggroup: GGroup): Group = ggroup match {

    case GGroup(name, email, Some(id), descOpt, Some(directMembersCount), _, Some(adminCreated)) =>
      CompleteGroup(email, name, id, directMembersCount, descOpt, adminCreated)

    case GGroup(name, email, idOpt, descOpt, directMembersCountOpt, _ , adminCreatedOpt) =>
      OptionGroup(email, Some(name), idOpt, directMembersCountOpt, descOpt, adminCreatedOpt )
  }

}

case class CompleteGroup(
                          email: String,
                          name: String,
                          id: String,
                          count: Long,
                          desc: Option[String],
                          adminCreated: Boolean
                        ) extends Group

object CompleteGroup {
  implicit def toGoogleApiScalaGroup(completeGroup: CompleteGroup): GGroup =
    GGroup(
      completeGroup.name,
      completeGroup.email,
      Some(completeGroup.id),
      completeGroup.desc,
      Some(completeGroup.count),
      None,
      Some(completeGroup.adminCreated)
    )

  implicit def toOptionGroup(completeGroup: CompleteGroup): OptionGroup =
    OptionGroup(
      completeGroup.email,
      Some(completeGroup.name),
      Some(completeGroup.id),
      Some(completeGroup.count),
      completeGroup.desc,
      Some(completeGroup.adminCreated)
    )
}

case class OptionGroup(
                        email: String,
                        name: Option[String],
                        id: Option[String],
                        count: Option[Long],
                        desc: Option[String],
                        adminCreated: Option[Boolean]
                      ) extends Group

case class GroupBuilder(
                         email: String,
                         name: Option[String],
                         desc: Option[String]
                       ) extends Group

