package edu.eckerd.google.scgapi.persistence.database

import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
/**
  * Created by davenpcm on 9/8/16.
  */
trait HasDB {
  implicit val dbConfig: DatabaseConfig[JdbcProfile]
  implicit lazy val profile = dbConfig.driver
  implicit lazy val db = dbConfig.db
}
