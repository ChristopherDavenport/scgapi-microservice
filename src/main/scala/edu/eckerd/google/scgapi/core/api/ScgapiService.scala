package edu.eckerd.google.scgapi.core.api

import edu.eckerd.google.scgapi.models.Group
import scala.concurrent.Future

/**
  * Created by davenpcm on 9/8/16.
  */
trait ScgapiService {
  def createGroup(group: Group) : Future[Group]
  def updateGroup(group: Group) : Future[Group]
  def deleteGroup(group: Group) : Future[Unit]

}