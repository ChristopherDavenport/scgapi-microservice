package edu.eckerd.google.scgapi.models

import edu.eckerd.google.api.services.directory.models.{Group => GGroup}

/**
  * Created by davenpcm on 9/8/16.
  */
sealed trait Group{
  val email: String
}

//object Group {
//
//  implicit def completeGroupToMatchedGroup(completeGroup: CompleteGroup): MatchedGroup =
//    MatchedGroup(
//      completeGroup.email,
//      completeGroup.name,
//      Some(completeGroup.id),
//      Some(completeGroup.count),
//      completeGroup.desc,
//      Some(completeGroup.adminCreated)
//    )
//
//}

case class CompleteGroup(
                          email: String,
                          name: String,
                          id: String,
                          count: Long,
                          desc: Option[String],
                          adminCreated: Boolean
                        ) extends Group

case class MatchedGroup(
                         email: String,
                         name: String,
                         id: Option[String],
                         count: Option[Long],
                         desc: Option[String],
                         adminCreated: Option[Boolean]
                       ) extends Group

case class GroupBuilder(
                         email: String,
                         name: String,
                         desc: Option[String]
                       ) extends Group

