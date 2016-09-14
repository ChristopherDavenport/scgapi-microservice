package edu.eckerd.google.scgapi.persistence.google.conversions

import edu.eckerd.google.api.services.directory.models.{Group => GGroup}
import edu.eckerd.google.scgapi.models.{CompleteGroup, Group, GroupBuilder, MatchedGroup}

/**
  * Created by Chris Davenport on 9/11/16.
  */
trait GroupConversions {

  private[google] def gGroupToGroup(gGroup: GGroup): Group =
    Group(
      gGroup.email,
      gGroup.name,
      gGroup.id,
      gGroup.directMemberCount,
      gGroup.description,
      gGroup.adminCreated
    )

  private[google] def matchedGroupToGGroup(matchedGroup: MatchedGroup): GGroup =
    GGroup(
      matchedGroup.name,
      matchedGroup.email,
      matchedGroup.id,
      matchedGroup.desc,
      matchedGroup.count,
      None,
      matchedGroup.adminCreated
    )

  private[google] def groupBuilderToGGroup(groupBuilder: GroupBuilder): GGroup =
    matchedGroupToGGroup(groupBuilder.asMatchedGroup)

  private[google] def completeGroupToGGroup(completeGroup: CompleteGroup): GGroup =
    matchedGroupToGGroup(completeGroup.asMatchedGroup)


}
