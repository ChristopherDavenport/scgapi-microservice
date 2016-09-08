package edu.eckerd.google.scgapi.persistence

import edu.eckerd.google.scgapi.persistence.api.DatabaseService
import edu.eckerd.google.scgapi.models.CompleteGroup
import edu.eckerd.google.scgapi.models.OptionGroup
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import scala.concurrent.Future

/**
  * Created by davenpcm on 9/8/16.
  */
class DatabaseServiceImpl extends DatabaseService with HasDB {
  val dbConfig: DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig("database")

  def groupExists(email: String): Future[Boolean] = ???
  def groupUpdate(optionGroup: OptionGroup): Future[Int] = ???
  def groupInsert(completeGroup: CompleteGroup): Future[Int] = ???
}