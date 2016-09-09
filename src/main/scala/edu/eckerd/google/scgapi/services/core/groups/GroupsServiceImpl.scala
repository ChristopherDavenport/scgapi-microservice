package edu.eckerd.google.scgapi.services.core.groups

import edu.eckerd.google.api.services.directory.Directory
import edu.eckerd.google.scgapi.models.{Group, GroupBuilder}
import edu.eckerd.google.scgapi.persistence.DatabaseService

import scala.concurrent.Future

/**
  * Created by davenpcm on 9/9/16.
  */
class GroupsServiceImpl(databaseService: DatabaseService,
                        directory: Directory) extends GroupsService {

  def createGroup(groupBuilder: GroupBuilder) : Future[Group] = ???

  def updateGroup(group: Group) : Future[Group] = ???

  def deleteGroupByEmail(email: String) : Future[Unit] = ???

}
