package edu.eckerd.google.scgapi.persistence.google

import com.typesafe.scalalogging.LazyLogging
import edu.eckerd.google.api.services.directory.Directory
import edu.eckerd.google.api.services.directory.models.{Group => GGroup}
import edu.eckerd.google.scgapi.models._
import edu.eckerd.google.scgapi.persistence.google.conversions.GroupConversions
import edu.eckerd.google.scgapi.persistence.google.core.groups.GroupsDirectoryServiceImpl

import scala.util.Try
/**
  * Created by davenpcm on 9/10/16.
  */
class DirectoryServiceImpl extends DirectoryService with GroupsDirectoryServiceImpl {
  val directory = Directory()
}

object DirectoryServiceImpl {
  def apply(): DirectoryServiceImpl = new DirectoryServiceImpl
}
