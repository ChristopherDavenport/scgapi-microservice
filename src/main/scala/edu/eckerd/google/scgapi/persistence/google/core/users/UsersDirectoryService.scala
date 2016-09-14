package edu.eckerd.google.scgapi.persistence.google.core.users

import edu.eckerd.google.scgapi.models._


/**
  * Created by davenpcm on 9/14/16.
  */
trait UsersDirectoryService {

  def getUser(userEmail: String): Option[User]
  def getUsers: Users
  def createUser(userBuilder: UserBuilder): User


}
