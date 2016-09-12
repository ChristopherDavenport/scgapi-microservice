package edu.eckerd.google.scgapi.persistence.google.core.groups

import edu.eckerd.google.api.services.directory.Directory
import edu.eckerd.google.scgapi.models.{Group, GroupBuilder}
import edu.eckerd.google.scgapi.persistence.google.conversions.GroupConversions

import scala.util.Try
/**
  * Created by davenpcm on 9/12/16.
  */
trait GroupsDirectoryServiceImpl extends GroupsDirectoryService with GroupConversions {
  val directory: Directory

  def createGroup(groupBuilder: GroupBuilder): Option[Group] = {
    val ggroup = groupBuilderToGGroup(groupBuilder)
    println(ggroup)
    val createdGroup = Try(directory.groups.create(ggroup))
    println(createdGroup)
    val finalGroup = createdGroup.toOption.map(gGroupToGroup)
    println(finalGroup)
    finalGroup
  }

  def deleteGroup(groupBuilder: GroupBuilder): Option[Unit] = {
    val ggroup = groupBuilderToGGroup(groupBuilder)
    val deleted = directory.groups.delete(groupBuilder.email)
    println(ggroup, deleted)
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
