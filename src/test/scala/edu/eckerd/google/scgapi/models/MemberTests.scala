package edu.eckerd.google.scgapi.models

import org.scalatest.{FlatSpec, Matchers}
import MemberRole.{MANAGER, OWNER, MEMBER}

/**
  * Created by davenpcm on 9/12/16.
  */
class MemberTests extends FlatSpec with Matchers {

  "apply" should "generate a CompleteMember if all fields are present" in {
    Member(Some("email"), Some("id"), "MANAGER", "USER") shouldEqual CompleteMember("email", "id", MANAGER, "USER")
  }

  it should "generate a MatchedMember if one of the fields is missing" in {
    Member(Some("email"), None, "MEMBER", "USER") shouldEqual MatchedMember(Some("email"), None, MEMBER, "USER")
  }

  it should "have a second apply for createing a MemberBuilder" in {
    Member("email", Some("OWNER")) shouldEqual MemberBuilder("email", Some(OWNER))
  }

}
