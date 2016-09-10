package edu.eckerd.google.scgapi.persistence.google

import edu.eckerd.google.scgapi.models.{Group, GroupBuilder}

/**
  * Created by davenpcm on 9/10/16.
  */
trait DirectoryService {
  def getGroup(email: String): Option[Group]
  def createGroup(groupBuilder: GroupBuilder): Option[Group]
  def deleteGroup(groupBuilder: GroupBuilder): Option[Unit]

}
