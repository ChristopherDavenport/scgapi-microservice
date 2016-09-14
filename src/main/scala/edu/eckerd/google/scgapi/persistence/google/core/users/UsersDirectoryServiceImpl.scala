package edu.eckerd.google.scgapi.persistence.google.core.users

import edu.eckerd.google.api.services.directory.Directory
import edu.eckerd.google.scgapi.models.{User, UserBuilder, Users}
import edu.eckerd.google.scgapi.persistence.google.conversions.UserConversions

/**
  * Created by davenpcm on 9/14/16.
  */
trait UsersDirectoryServiceImpl extends UserConversions {
  val directory: Directory

  def getUser(userEmail: String): Option[User] = directory.users.get(userEmail) match {
    case Right(user) => Option(gUserToUser(user))
    case Left(error) =>
      println(error.getLocalizedMessage)
      None
  }
  def getUsers: Users = {
    val listUsers = directory.users.list("eckerd.edu").map(gUserToUser)
    Users(listUsers)
  }
  def createUser(userBuilder: UserBuilder): User = {
    val gUser = userToGUser(userBuilder)
    val createdGUser = directory.users.create(gUser)

    gUserToUser(createdGUser)
  }


}
