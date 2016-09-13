package edu.eckerd.google.scgapi.persistence.google.core.groups

import edu.eckerd.google.scgapi.models.{Group, GroupBuilder}

/**
  * Created by Chris Davenport on 9/12/16.
  */
trait GroupsDirectoryService {
  def getGroup(email: String): Option[Group]
  def createGroup(groupBuilder: GroupBuilder): Option[Group]
  def deleteGroup(groupBuilder: GroupBuilder): Option[Unit]
}
