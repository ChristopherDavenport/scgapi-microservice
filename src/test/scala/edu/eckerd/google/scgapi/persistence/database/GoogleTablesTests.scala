package edu.eckerd.google.scgapi.persistence.database

import org.scalatest.{AsyncFlatSpec, FlatSpec, Matchers}
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import edu.eckerd.google.api.services.directory.models.{Email => gEmail, Name => gName, User => gUser}
import org.h2.jdbc.JdbcSQLException

import scala.concurrent.Await
import scala.concurrent.duration._


/**
  * Created by davenpcm on 9/11/16.
  */
class GoogleTablesTests extends AsyncFlatSpec with Matchers with HasDB with GoogleTables {
  val dbConfig: DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig("database")
  import profile.api._

  val ggRow = GoogleGroupsRow("groupid", "y", "name", "email", 4L, Some("desc"))
  val ggRow2 = GoogleGroupsRow("groupid2", "y", "name", "email", 4L, Some("desc"))
  val ggUser = gUser(
    gName("Chris", "Davenport"),
    gEmail("email"),
    None,
    Some("userId"),
    "",
    None,
    false, true, true, false, true, false
  )
  val ggMember = GoogleMembersRow("groupid","userId", Some("email"), "Y", "MEMBER", "USER")

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
