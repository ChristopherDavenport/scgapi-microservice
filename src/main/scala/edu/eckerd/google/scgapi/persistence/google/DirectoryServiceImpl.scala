package edu.eckerd.google.scgapi.persistence.google

import edu.eckerd.google.api.services.directory.Directory
import edu.eckerd.google.api.services.directory.models.{Group => GGroup}
import edu.eckerd.google.scgapi.models._

import scala.util.Try
/**
  * Created by davenpcm on 9/10/16.
  */
class DirectoryServiceImpl extends DirectoryService {

  val directory = Directory()

  private[google] def groupBuilderToGGroup(groupBuilder: GroupBuilder): GGroup = {
    GGroup(
      name = groupBuilder.name,
      email = groupBuilder.email,
      id = None,
      description = groupBuilder.desc,
      directMemberCount = None,
      members = None,
      adminCreated = Some(true)
    )
  }

 private[google] def matchedGroupToGGroup(matchedGroup: MatchedGroup): GGroup =
   GGroup(
     matchedGroup.name,
     matchedGroup.email,
     matchedGroup.id,
     matchedGroup.desc,
     matchedGroup.count,
     None,
     matchedGroup.adminCreated
   )

  private[google] def completeGroupToGGroup(completeGroup: CompleteGroup): GGroup =
    GGroup(
      completeGroup.name,
      completeGroup.email,
      Some(completeGroup.id),
      completeGroup.desc,
      Some(completeGroup.count),
      None,
      Some(completeGroup.adminCreated)
    )

  private[google] def gGrouptoGroup(gGroup: GGroup): Group = gGroup match {

    case GGroup(name, email, Some(id), descOpt, Some(directMembersCount), _, Some(adminCreated)) =>
      CompleteGroup(
        email,
        name,
        id,
        directMembersCount,
        descOpt,
        adminCreated
      )
    case GGroup(name, email, idOpt, descOpt, directMembersCountOpt, _ , adminCreatedOpt) =>
      MatchedGroup(
        email,
        name,
        idOpt,
        directMembersCountOpt,
        descOpt,
        adminCreatedOpt
      )
  }

  def createGroup(groupBuilder: GroupBuilder): Option[Group] = {
    val ggroup = groupBuilderToGGroup(groupBuilder)
    val createdGroup = Try(directory.groups.create(ggroup))
    println(ggroup)
    println(createdGroup)
    val finalGroup = createdGroup.toOption.map(gGrouptoGroup)
    println(finalGroup)
    finalGroup
  }

  def deleteGroup(groupBuilder: GroupBuilder): Option[Unit] = None

//  def deleteGroup(groupBuilder: GroupBuilder): Option[Unit] = {
//    val ggroup = groupBuilderToGGroup(groupBuilder)
//    val deleted = directory.groups.delete(groupBuilder.email)
//    println(ggroup, deleted)
//    deleted.toOption
//  }

  def getGroup(email: String): Option[Group] = {
    val gGroupOpt = directory.groups.get(email)
      .toOption
    println(gGroupOpt)
    val groupOpt = gGroupOpt.map(gGrouptoGroup)
    println(groupOpt)
    groupOpt
  }


}

object DirectoryServiceImpl {
  def apply(): DirectoryServiceImpl = new DirectoryServiceImpl
}
