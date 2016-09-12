package edu.eckerd.google.scgapi.services.core.groups

import edu.eckerd.google.scgapi.models.{Group, GroupBuilder, MatchedGroup}
import edu.eckerd.google.scgapi.persistence.google.DirectoryService
import edu.eckerd.google.scgapi.persistence.google.core.groups.GroupsDirectoryService

import scala.concurrent.{ExecutionContext, Future}
/**
  * Created by davenpcm on 9/10/16.
  */
class GroupsServiceBasicImpl(groupsDirectoryService: GroupsDirectoryService)
                            (implicit executionContext: ExecutionContext) extends GroupsService {

  def getGroupByEmail(email: String)          : Future[Option[Group]] =
    Future{ groupsDirectoryService.getGroup(email) }
  def createGroup(groupBuilder: GroupBuilder) : Future[Option[Group]] =
    Future{ groupsDirectoryService.createGroup(groupBuilder) }
  def deleteGroup(groupBuilder: GroupBuilder) : Future[Unit]  =
    Future{ groupsDirectoryService.deleteGroup(groupBuilder)}

}

object GroupsServiceBasicImpl {
  def apply(directoryService: DirectoryService)(implicit executionContext: ExecutionContext): GroupsServiceBasicImpl =
    new GroupsServiceBasicImpl(directoryService)(executionContext)
}
