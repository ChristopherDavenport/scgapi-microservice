package edu.eckerd.google.scgapi.persistence.google.core.groups

import edu.eckerd.google.api.services.directory.Directory
import edu.eckerd.google.scgapi.models.{CompleteGroup, Group, GroupBuilder, MatchedGroup}

import scala.util.Try
/**
  * Created by Chris Davenport on 9/12/16.
  */
trait GroupsDirectoryServiceImpl extends GroupsDirectoryService with GroupConversions {
  val directory: Directory

  def createGroup(groupBuilder: GroupBuilder): Option[Group] = {
    val gGroup = groupBuilderToGGroup(groupBuilder)
    println(gGroup)
    val createdGroup = Try(directory.groups.create(gGroup))
    println(createdGroup)
    val finalGroup = createdGroup.toOption.map(gGroupToGroup)
    println(finalGroup)
    finalGroup match {
      case Some(MatchedGroup(email, name, Some(id), None, Some(desc), Some(adminCreated) )) =>
        Some(CompleteGroup(email, name, id, 0L, Some(desc), adminCreated))
      case _ => finalGroup
    }

  }

  def deleteGroup(groupBuilder: GroupBuilder): Option[Unit] = {
    val gGroup = groupBuilderToGGroup(groupBuilder)
    val deleted = directory.groups.delete(groupBuilder.email)
    println(gGroup, deleted)
    deleted.toOption
  }

  def getGroup(email: String): Option[Group] = {
    val gGroupOpt = directory.groups.get(email)
      .toOption
    println(gGroupOpt)
    val groupOpt = gGroupOpt.map(gGroupToGroup)
    println(groupOpt)
    groupOpt
  }

}
