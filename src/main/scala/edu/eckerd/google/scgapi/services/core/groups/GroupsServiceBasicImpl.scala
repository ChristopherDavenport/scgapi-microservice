package edu.eckerd.google.scgapi.services.core.groups

import edu.eckerd.google.scgapi.models.{Group, GroupBuilder, MatchedGroup}
import edu.eckerd.google.scgapi.persistence.google.DirectoryService

import scala.concurrent.{ExecutionContext, Future}
/**
  * Created by davenpcm on 9/10/16.
  */
class GroupsServiceBasicImpl(directoryService: DirectoryService)
                            (implicit executionContext: ExecutionContext) extends GroupsService {

  def getGroupByEmail(email: String)          : Future[Option[Group]] = Future{ directoryService.getGroup(email) }
  def createGroup(groupBuilder: GroupBuilder) : Future[Group] = ???
//  def updateGroup(groupBuilder: GroupBuilder) : Future[Group] = ???
  def deleteGroup(groupBuilder: GroupBuilder) : Future[Unit]  = ???

}

object GroupsServiceBasicImpl {
  def apply(directoryService: DirectoryService)(implicit executionContext: ExecutionContext): GroupsServiceBasicImpl =
    new GroupsServiceBasicImpl(directoryService)(executionContext)
}
