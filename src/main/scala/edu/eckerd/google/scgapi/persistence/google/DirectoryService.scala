package edu.eckerd.google.scgapi.persistence.google

import edu.eckerd.google.scgapi.persistence.google.core.groups.GroupsDirectoryService
import edu.eckerd.google.scgapi.persistence.google.core.members.MembersDirectoryService

/**
  * Created by Chris Davenport on 9/10/16.
  */
trait DirectoryService extends GroupsDirectoryService with MembersDirectoryService
