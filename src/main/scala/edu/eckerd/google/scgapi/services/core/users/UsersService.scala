package edu.eckerd.google.scgapi.services.core.users

import edu.eckerd.google.scgapi.models.{User, UserBuilder, Users}

import scala.concurrent.Future

/**
  * Created by Chris Davenport on 9/9/16.
  */
trait UsersService {

  def getUser(userEmail: String): Future[Option[User]]
  def getUsers: Future[Users]
  def createUser(userBuilder: UserBuilder): Future[User]

}
