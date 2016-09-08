package edu.eckerd.google.scgapi.persistence.api

import edu.eckerd.google.scgapi.models.CompleteGroup
import edu.eckerd.google.scgapi.models.OptionGroup
import scala.concurrent.Future
/**
  * Created by davenpcm on 9/8/16.
  */
trait DatabaseService {
  def groupExists(email: String): Future[Boolean]
  def groupUpdate(optionGroup: OptionGroup): Future[Int]
  def groupInsert(completeGroup: CompleteGroup): Future[Int]

}
