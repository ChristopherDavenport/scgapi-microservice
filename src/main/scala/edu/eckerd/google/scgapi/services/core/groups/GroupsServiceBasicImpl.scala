package edu.eckerd.google.scgapi.services.core.groups

import edu.eckerd.google.scgapi.models.{Group, GroupBuilder}
import edu.eckerd.google.scgapi.persistence.google.DirectoryService
import edu.eckerd.google.scgapi.persistence.google.core.groups.GroupsDirectoryService
import edu.eckerd.google.scgapi.services.core.CoreFunctions

import scala.concurrent.{ExecutionContext, Future}
/**
  * Created by Chris Davenport on 9/10/16.
  */
class GroupsServiceBasicImpl(groupsDirectoryService: GroupsDirectoryService)
                            (implicit executionContext: ExecutionContext) extends GroupsService with CoreFunctions{

  def getGroupByEmail(email: String)          : Future[Option[Group]] = Future{
    groupsDirectoryService.getGroup( emailParse(email) )
  }

  def createGroup(groupBuilder: GroupBuilder) : Future[Option[Group]] = Future{
    groupsDirectoryService.createGroup(groupBuilder)
  }

  def deleteGroup(groupBuilder: GroupBuilder) : Future[Unit]  = Future{
    groupsDirectoryService.deleteGroup(groupBuilder)
  }

}

object GroupsServiceBasicImpl {
  def apply(directoryService: DirectoryService)(implicit executionContext: ExecutionContext): GroupsServiceBasicImpl =
    new GroupsServiceBasicImpl(directoryService)(executionContext)
}
