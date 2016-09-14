package edu.eckerd.google.scgapi.services.core.users

import edu.eckerd.google.scgapi.models.{ErrorResponse, User, UserBuilder, Users}

import scala.concurrent.Future

/**
  * Created by Chris Davenport on 9/9/16.
  */
trait UsersService {

  def getUser(userEmail: String): Future[Either[ErrorResponse, User]]
  def getUsers: Future[Either[ErrorResponse, Users]]
  def createUser(userBuilder: UserBuilder): Future[Either[ErrorResponse, User]]

}
