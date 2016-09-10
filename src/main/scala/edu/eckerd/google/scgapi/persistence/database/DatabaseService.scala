package edu.eckerd.google.scgapi.persistence.database

import edu.eckerd.google.scgapi.models.{CompleteGroup, MatchedGroup}

import scala.concurrent.Future
/**
  * Created by davenpcm on 9/8/16.
  */
trait DatabaseService {
  def groupExists(email: String): Future[Boolean]
  def groupUpdate(optionGroup: MatchedGroup): Future[Int]
  def groupInsert(completeGroup: CompleteGroup): Future[Int]

}
