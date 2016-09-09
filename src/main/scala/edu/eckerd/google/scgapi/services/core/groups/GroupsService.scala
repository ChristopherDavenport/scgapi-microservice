package edu.eckerd.google.scgapi.services.core.groups

import edu.eckerd.google.scgapi.models.{Group, GroupBuilder}

import scala.concurrent.Future

/**
  * Created by davenpcm on 9/9/16.
  */
trait GroupsService {
  def createGroup(group: GroupBuilder) : Future[Group]
  def updateGroup(group: Group) : Future[Group]
  def deleteGroupByEmail(email: String) : Future[Unit]
}
