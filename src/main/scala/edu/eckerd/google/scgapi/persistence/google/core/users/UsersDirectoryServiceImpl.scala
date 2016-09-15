package edu.eckerd.google.scgapi.persistence.google.core.users

import edu.eckerd.google.api.services.directory.Directory
import edu.eckerd.google.scgapi.models.{ErrorResponse, User, UserBuilder, Users}
import edu.eckerd.google.scgapi.models.ErrorResponse._
import edu.eckerd.google.scgapi.persistence.google.core.shared.GoogleErrorHandling

import scala.util.{Failure, Success, Try}

/**
  * Created by davenpcm on 9/14/16.
  */
trait UsersDirectoryServiceImpl extends UsersDirectoryService with UserConversions with GoogleErrorHandling {
  val directory: Directory



  def getUser(userEmail: String): Either[ErrorResponse, User] = {
    directory.users.get(userEmail) match {
      case Right(user) => Right[ErrorResponse, User](gUserToUser(user))
      case Left(e) => e.getLocalizedMessage match {
        case tooManyRequests  if tooManyRequests.contains("429")  => Left(TooManyRequests)
        case notFound         if notFound.contains("404")         => Left(NotFound)
        case conflict         if conflict.contains("409")         => Left(Conflict)
        case _                                                    => Left(InternalServerError)
      }
    }
  }

  def getUsers: Either[ErrorResponse, Users] = handleGoogleErrors{
    val listUsers = directory.users.list("eckerd.edu").map(gUserToUser)
    Users(listUsers)
  }

  def createUser(userBuilder: UserBuilder): Either[ErrorResponse, User] = handleGoogleErrors{
    val gUser = userToGUser(userBuilder)
    val createdGUser = directory.users.create(gUser)

    gUserToUser(createdGUser)
  }


}
