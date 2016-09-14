package edu.eckerd.google.scgapi.models

import org.scalatest.{FlatSpec, Matchers}
import MemberRoles.{MANAGER, OWNER, MEMBER}
import MemberTypes.{CUSTOMER, GROUP, USER}

/**
  * Created by Chris Davenport on 9/12/16.
  */
class MemberTests extends FlatSpec with Matchers {

  "apply" should "generate a CompleteMember if all fields are present" in {
    Member(Some("email"), Some("id"), MANAGER, USER) shouldEqual CompleteMember("email", "id", MANAGER, USER)
  }

  it should "generate a MatchedMember if the email field is missing" in {
    Member(None, Some("id"), MEMBER, GROUP) shouldEqual MatchedMember(None, Some("id"), MEMBER, GROUP)
  }

  it should "create a MemberBuilder if " in {
    Member(Some("email"), None, OWNER, CUSTOMER) shouldEqual MemberBuilder("email", OWNER, CUSTOMER)
  }

}
