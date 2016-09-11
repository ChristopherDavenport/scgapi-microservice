package edu.eckerd.google.scgapi.persistence.google

import edu.eckerd.google.api.services.directory.Directory
import edu.eckerd.google.api.services.directory.models.{Group => GGroup}
import edu.eckerd.google.scgapi.models._
import edu.eckerd.google.scgapi.persistence.google.conversions.GroupConversions

import scala.util.Try
/**
  * Created by davenpcm on 9/10/16.
  */
class DirectoryServiceImpl extends DirectoryService with GroupConversions {

  val directory = Directory()

  def createGroup(groupBuilder: GroupBuilder): Option[Group] = {
    val ggroup = groupBuilderToGGroup(groupBuilder)
    val createdGroup = Try(directory.groups.create(ggroup))
    println(ggroup)
    println(createdGroup)
    val finalGroup = createdGroup.toOption.map(gGroupToGroup)
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
    val groupOpt = gGroupOpt.map(gGroupToGroup)
    println(groupOpt)
    groupOpt
  }


}

object DirectoryServiceImpl {
  def apply(): DirectoryServiceImpl = new DirectoryServiceImpl
}
