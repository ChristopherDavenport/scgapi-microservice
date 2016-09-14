package edu.eckerd.google.scgapi.persistence.google.core.users

import edu.eckerd.google.scgapi.models._


/**
  * Created by davenpcm on 9/14/16.
  */
trait UsersDirectoryService {

  def getUser(userEmail: String): Either[ErrorResponse, User]
  def getUsers: Either[ErrorResponse, Users]
  def createUser(userBuilder: UserBuilder): Either[ErrorResponse, User]


}
