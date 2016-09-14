package edu.eckerd.google.scgapi.persistence.google

import edu.eckerd.google.api.services.directory.Directory
import edu.eckerd.google.scgapi.persistence.google.core.groups.GroupsDirectoryServiceImpl
import edu.eckerd.google.scgapi.persistence.google.core.members.MembersDirectoryServiceImpl
import edu.eckerd.google.scgapi.persistence.google.core.users.UsersDirectoryServiceImpl

/**
  * Created by Chris Davenport on 9/10/16.
  */
class DirectoryServiceImpl extends DirectoryService
  with GroupsDirectoryServiceImpl with MembersDirectoryServiceImpl
  with UsersDirectoryServiceImpl {
  val directory = Directory()
}

object DirectoryServiceImpl {
  def apply(): DirectoryServiceImpl = new DirectoryServiceImpl
}
