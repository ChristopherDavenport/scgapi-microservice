//package edu.eckerd.google.scgapi.services.core.groups
//
//import edu.eckerd.google.api.services.directory.Directory
//import edu.eckerd.google.scgapi.models.{Group, GroupBuilder, MatchedGroup}
//import edu.eckerd.google.scgapi.persistence.database.DatabaseService
//import edu.eckerd.google.scgapi.persistence.google.DirectoryService
//
//import scala.concurrent.Future
//
///**
//  * Created by davenpcm on 9/9/16.
//  */
//class GroupsServiceImpl(
//                         databaseService: DatabaseService,
//                         directory: DirectoryService
//                       )
//  extends GroupsService {
//
//  def getGroupByEmail(email: String)          : Future[Option[Group]] = ???
//  def createGroup(groupBuilder: GroupBuilder) : Future[Option[Group]] = ???
////  def updateGroup(groupBuilder: GroupBuilder) : Future[Group] = ???
//  def deleteGroup(groupBuilder: GroupBuilder) : Future[Unit]  = ???
//
//}
