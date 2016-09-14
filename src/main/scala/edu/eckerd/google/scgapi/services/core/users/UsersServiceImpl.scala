package edu.eckerd.google.scgapi.services.core.users

import edu.eckerd.google.scgapi.models.{ErrorResponse, User, UserBuilder, Users}
import edu.eckerd.google.scgapi.persistence.google.core.users.UsersDirectoryService
import edu.eckerd.google.scgapi.services.core.CoreFunctions

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Chris Davenport on 9/9/16.
  */
class UsersServiceImpl(usersDirectoryService: UsersDirectoryService)
                      (implicit executionContext: ExecutionContext) extends UsersService with CoreFunctions {

  def getUser(userEmail: String): Future[Either[ErrorResponse, User]] = Future{
    val parsedEmail = emailParse(userEmail)
    usersDirectoryService.getUser(parsedEmail)
  }
  def getUsers: Future[Either[ErrorResponse, Users]] = Future{
    usersDirectoryService.getUsers
  }

  def createUser(userBuilder: UserBuilder): Future[Either[ErrorResponse,User]] = Future{
    val userBuilderWithParsedEmail = userBuilder.copy(email = emailParse(userBuilder.email))
    usersDirectoryService.createUser(userBuilderWithParsedEmail)
  }

}

object UsersServiceImpl {

  def apply(usersDirectoryService: UsersDirectoryService)
           (implicit executionContext: ExecutionContext): UsersServiceImpl =
    new UsersServiceImpl(usersDirectoryService)(executionContext)

}
