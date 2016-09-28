package edu.eckerd.google.scgapi.persistence.google.core.groups

import edu.eckerd.google.scgapi.models.{ErrorResponse, Group, GroupBuilder}

/**
  * Created by Chris Davenport on 9/12/16.
  */
trait GroupsDirectoryService {
  def getGroup(email: String): Either[ErrorResponse, Group]
  def createGroup(groupBuilder: GroupBuilder): Either[ErrorResponse, Group]
  def deleteGroup(groupBuilder: GroupBuilder): Either[ErrorResponse, Unit]
}
