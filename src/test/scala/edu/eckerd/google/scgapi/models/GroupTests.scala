package edu.eckerd.google.scgapi.models

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by Chris Davenport on 9/11/16.
  */
class GroupTests extends FlatSpec with Matchers {

  val trueAdminCreated = true

  "Group apply" should "create a Complete Group if all fields are present" in {
    val cg = CompleteGroup("email", "name", "id", 1L, Some("desc"), trueAdminCreated)
    Group(cg.email, cg.name, cg.id, cg.count, cg.desc, cg.adminCreated) shouldEqual cg
  }

  it should "create a GroupBuilder if Only the core trait attributes are present" in {
    val gb = GroupBuilder("email", "name", Some("desc"))
    Group(gb.email, gb.name, gb.desc) shouldEqual gb
  }

  it should "create a Matched Group if any of the none core attributes are present" in {
    def matchedGroupCreator(
                             idOpt: Option[String],
                             countOpt: Option[Long],
                             adminCreatedOpt: Option[Boolean]
                           ): MatchedGroup = {
      MatchedGroup("email", "name", idOpt, countOpt, Some("desc"), adminCreatedOpt)
    }
    val mg1 = matchedGroupCreator(Some("id"), Some(4L), Some(true))
    Group(mg1.email, mg1.name, mg1.id, mg1.count, mg1.desc, mg1.adminCreated) should not be mg1

    val mg2 = matchedGroupCreator(None, Some(4L), Some(true))
    Group(mg2.email, mg2.name, mg2.id, mg2.count, mg2.desc, mg2.adminCreated) shouldEqual mg2

    val mg5 = matchedGroupCreator(Some("id"), None, Some(true))
    Group(mg5.email, mg5.name, mg5.id, mg5.count, mg5.desc, mg5.adminCreated) shouldEqual mg5

    val mg6 = matchedGroupCreator(Some("id"), Some(4L), None)
    Group(mg6.email, mg6.name, mg6.id, mg6.count, mg6.desc, mg6.adminCreated) shouldEqual mg6

    val mg3 = matchedGroupCreator(None, None, Some(true))
    Group(mg3.email, mg3.name, mg3.id, mg3.count, mg3.desc, mg3.adminCreated) shouldEqual mg3

    val mg7 = matchedGroupCreator(None, Some(4L), None)
    Group(mg7.email, mg7.name, mg7.id, mg7.count, mg7.desc, mg7.adminCreated) shouldEqual mg7

    val mg8 = matchedGroupCreator(Some("id"), None, None)
    Group(mg8.email, mg8.name, mg8.id, mg8.count, mg8.desc, mg8.adminCreated) shouldEqual mg8

    val mg4 = matchedGroupCreator(None, None, None)
    Group(mg4.email, mg4.name, mg4.id, mg4.count, mg4.desc, mg4.adminCreated) should not be mg4
  }

  it should "create a CompleteGroup if all optional values are present in the option constructor pattern" in {
    val cg = CompleteGroup("email", "name", "id", 1L, Some("desc"), trueAdminCreated)
    Group("email", "name", Some("id"), Some(1L), Some("desc"), Some(true)) shouldEqual cg
  }

  it should "create a GroupBuilder if non of the optional values are present in the option constructor pattern" in {
    val e = "email"
    val n = "name"
    val d = Some("desc")
    // All Optional Traits Are Absent
    val mg = MatchedGroup(e, n, None, None, d, None)
    val gb = GroupBuilder(e, n, d)
    Group(mg.email, mg.name, mg.id, mg.count, mg.desc, mg.adminCreated) shouldEqual gb
  }

  "asGroupBuilder" should "convert CompleteGroup to GroupBuilder" in {
    val e = "email"
    val n = "name"
    val d = Some("desc")
    val gb = GroupBuilder(e, n, d)
    val cg = CompleteGroup(e,n, "id", 4L, Some("desc"), trueAdminCreated)
    cg.asGroupBuilder shouldEqual gb
  }

  it should "convert a MatchedGroup to GroupBuilder" in {
    val e = "email"
    val n = "name"
    val d = Some("desc")
    val mg = MatchedGroup(e, n, Some("id"), None, d, None)
    val gb = GroupBuilder(e, n, d)
    mg.asGroupBuilder shouldEqual gb
  }

  it should "be idempotent on GroupBuilder" in {
    val e = "email"
    val n = "name"
    val d = Some("desc")
    val gb = GroupBuilder(e,n,d)
    gb.asGroupBuilder shouldEqual gb
  }

  "asMatchedGroup" should "convert CompleteGroup to MatchedGroup" in {
    val cg = CompleteGroup("email","name", "id", 4L, Some("desc"), trueAdminCreated)
    val mg = MatchedGroup("email", "name", Some("id"), Some(4L), Some("desc"), Some(true))
    cg.asMatchedGroup shouldEqual mg
  }

  it should "convert GroupBuilder to MatchedGroup" in {
    val gb = GroupBuilder("email", "name", Some("desc"))
    val mg = MatchedGroup("email", "name", None, None, Some("desc"), None)
    gb.asMatchedGroup shouldEqual mg
  }

  it should "be idempotent on MatchedGroup" in {
    val mg = MatchedGroup("email", "name", Some("id"), None, Some("desc"), Some(true))
    mg.asMatchedGroup shouldEqual mg
  }

}
