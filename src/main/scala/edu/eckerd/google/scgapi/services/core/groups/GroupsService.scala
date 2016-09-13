package edu.eckerd.google.scgapi.services.core.groups

import edu.eckerd.google.scgapi.models.{Group, GroupBuilder, MatchedGroup}

import scala.concurrent.Future

/**
  * Created by Chris Davenport on 9/9/16.
  */
trait GroupsService {
  def getGroupByEmail(email: String)          : Future[Option[Group]]
  def createGroup(groupBuilder: GroupBuilder) : Future[Option[Group]]
//  def updateGroup(groupBuilder: GroupBuilder) : Future[Group]
  def deleteGroup(groupBuilder: GroupBuilder) : Future[Unit]
}
