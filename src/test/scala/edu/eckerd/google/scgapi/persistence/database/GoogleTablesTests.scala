package edu.eckerd.google.scgapi.persistence.database

import edu.eckerd.google.api.services.directory.models.{Email => gEmail, Name => gName, User => gUser}
import org.h2.jdbc.JdbcSQLException
import org.scalatest.{AsyncFlatSpec, Matchers}
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile

import scala.concurrent.Await
import scala.concurrent.duration._


/**
  * Created by Chris Davenport on 9/11/16.
  */
class GoogleTablesTests extends AsyncFlatSpec with Matchers with HasDB with GoogleTables {
  val dbConfig: DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig("database")
  import profile.api._

  val ggRow = GoogleGroupsRow("groupId", "y", "name", "email", 4L, Some("desc"))
  val ggRow2 = GoogleGroupsRow("groupId2", "y", "name", "email", 4L, Some("desc"))
  object ggUserBooleans {
    val changePasswordFalse = false
    val includeInGlobalAddressTrue = true
    val ipWhiteListedTrue = true
    val isAdminFalse = false
    val isMailboxSetupTrue = true
    val isSuspendedFalse = false
  }
  val ggUser = gUser(
    gName("Chris", "Davenport"),
    gEmail("email"),
    None,
    Some("userId"),
    "",
    None,
    ggUserBooleans.changePasswordFalse,
    ggUserBooleans.includeInGlobalAddressTrue,
    ggUserBooleans.ipWhiteListedTrue,
    ggUserBooleans.isAdminFalse,
    ggUserBooleans.isMailboxSetupTrue,
    ggUserBooleans.isSuspendedFalse
  )
  val ggMember = GoogleMembersRow("groupId","userId", Some("email"), "Y", "MEMBER", "USER")

  "schema.create" should "create the schema" in {

    db.run(schema.create).flatMap{ _ =>
      db.run(googleGroups += ggRow).flatMap { _ =>
        db.run(googleGroups.filter(_.id === ggRow.id).exists.result).map(bool => assert(bool))
      }
    }
  }

  "GroupsTable" should "be able to add a row" in {
    for {
      _ <- db.run(googleGroups += ggRow2)
      bool <- db.run(googleGroups.filter(_.id === ggRow2.id).exists.result)
    } yield assert(bool)
  }

  it should "be able to retrieve a row" in {
    for {
      result <- db.run(googleGroups.filter(_.id === ggRow2.id).result.headOption)
    } yield assert(result.contains(ggRow2))
  }

  "UsersTable" should "be able to add a row" in {
    for {
      _ <- db.run(googleUsers += ggUser)
      bool <- db.run(googleUsers.filter(_.googleID === ggUser.id).exists.result)
    } yield assert(bool)
  }

  it should "be able to retrieve a row" in {
    for {
      result <- db.run(googleUsers.filter(_.googleID === ggUser.id).result.headOption)
    } yield assert(result.contains(ggUser))
  }

  "MembersTable" should "be able to add a row" in {
    for {
      _ <- db.run(googleMembers += ggMember)
      bool <- db.run(googleMembers.filter(_.userID === ggMember.userID).exists.result)
    } yield assert(bool)
  }

  it should "be able to retrieve a row" in {
    for {
      result <- db.run(googleMembers.filter(_.userID === ggMember.userID).result.headOption)
    } yield assert(result.contains(ggMember))
  }

  "schema.drop" should "drop the schema" in {
    Await.result(db.run(schema.drop), 2.seconds)
    assertThrows[JdbcSQLException](Await.result(db.run(googleGroups += ggRow), 2.seconds))
  }


}
