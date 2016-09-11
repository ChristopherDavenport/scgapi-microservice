package edu.eckerd.google.scgapi.persistence.google.conversions

import org.scalatest.{FlatSpec, Matchers}
import edu.eckerd.google.scgapi.models.{Group, CompleteGroup, MatchedGroup, GroupBuilder}
import edu.eckerd.google.api.services.directory.models.{Group => GGroup}

/**
  * Created by davenpcm on 9/11/16.
  */
class GroupConversionsTests extends FlatSpec with Matchers with GroupConversions {

//  GGroup(
//    name = groupBuilder.name,
//    email = groupBuilder.email,
//    id = None,
//    description = groupBuilder.desc,
//    directMemberCount = None,
//    members = None,
//    adminCreated = Some(true)
//  )
  "gGroupToGroup" should "convert a gGroup with all options missing to a GroupBuilder" in {
    val gb = GroupBuilder("email", "name", Some("desc"))
    val gg = GGroup(
      "name",
      "email",
      None,
      Some("desc"),
      None,
      None,
      None
    )
    gGroupToGroup(gg) shouldEqual gb
  }

  it should "convert a gGroup with some options present to a MatchedGroup" in {
    val mg = MatchedGroup("email", "name", Some("id"), Some(4L), Some("desc"), None)
    val gg = GGroup(
      "name",
      "email",
      Some("id"),
      Some("desc"),
      Some(4L),
      None,
      None
    )
    gGroupToGroup(gg) shouldEqual mg
  }

  it should "convert a gGroup with all options present to a CompleteGroup" in {
    val cg = CompleteGroup("email", "name", "id", 4L, Some("desc"), true)
    val gg = GGroup(
      "name",
      "email",
      Some("id"),
      Some("desc"),
      Some(4L),
      None,
      Some(true)
    )
    gGroupToGroup(gg) shouldEqual cg
  }


  "matchedGroupToGGroup" should "convert a matchedGroup to a GGroup" in {
    val mg = MatchedGroup("email", "name", Some("id"), Some(4L), Some("desc"), None)
    val gg = GGroup(
      "name",
      "email",
      Some("id"),
      Some("desc"),
      Some(4L),
      None,
      None
    )
    matchedGroupToGGroup(mg) shouldEqual gg
  }

  "completeGroupToGGroup" should "convert a completeGroup to a GGroup" in {
    val cg = CompleteGroup("email", "name", "id", 4L, Some("desc"), true)
    val gg = GGroup(
      "name",
      "email",
      Some("id"),
      Some("desc"),
      Some(4L),
      None,
      Some(true)
    )
    completeGroupToGGroup(cg) shouldEqual gg
  }

  "groupBuilderToGGroup" should "convert a GroupBuilder to a GGroup" in {
    val gb = GroupBuilder("email", "name", Some("desc"))
    val gg = GGroup(
      "name",
      "email",
      None,
      Some("desc"),
      None,
      None,
      None
    )
    groupBuilderToGGroup(gb) shouldEqual gg
  }

}
