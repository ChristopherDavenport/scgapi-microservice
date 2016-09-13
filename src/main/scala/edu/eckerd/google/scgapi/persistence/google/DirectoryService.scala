package edu.eckerd.google.scgapi.persistence.google

import edu.eckerd.google.api.services.directory.models.{Group => GGroup}
import edu.eckerd.google.scgapi.models.{CompleteGroup, Group, GroupBuilder, MatchedGroup}
import edu.eckerd.google.scgapi.persistence.google.conversions.GroupConversions
import edu.eckerd.google.scgapi.persistence.google.core.groups.GroupsDirectoryService
import edu.eckerd.google.scgapi.persistence.google.core.members.MembersDirectoryService

/**
  * Created by davenpcm on 9/10/16.
  */
trait DirectoryService extends GroupsDirectoryService with MembersDirectoryService {


}
