package edu.eckerd.google.scgapi.persistence.google.core.groups

import edu.eckerd.google.api.services.directory.Directory
import edu.eckerd.google.scgapi.models.ErrorResponse.{Conflict, InternalServerError, NotFound, TooManyRequests}
import edu.eckerd.google.scgapi.models._
import edu.eckerd.google.scgapi.persistence.google.core.shared.GoogleErrorHandling
import cats.implicits._

/**
  * Created by Chris Davenport on 9/12/16.
  */
trait GroupsDirectoryServiceImpl extends GroupsDirectoryService with GroupConversions with GoogleErrorHandling {
  val directory: Directory

  def createGroup(groupBuilder: GroupBuilder): Either[ErrorResponse, Group] = handleGoogleErrors{
    val gGroup = groupBuilderToGGroup(groupBuilder)
    val createdGroup = gGroupToGroup(directory.groups.create(gGroup))

    createdGroup match {
      case MatchedGroup(email, name, Some(id), None, Some(desc), Some(adminCreated) ) =>
        CompleteGroup(email, name, id, 0L, Some(desc), adminCreated)
      case group => group
    }

  }

  def deleteGroup(groupBuilder: GroupBuilder): Either[ErrorResponse, Unit] = handleGoogleErrors{
    val gGroup = groupBuilderToGGroup(groupBuilder)
    val deleted = directory.groups.delete(groupBuilder.email)
    println(gGroup, deleted)
  }

  def getGroup(email: String): Either[ErrorResponse, Group] = {
    val gGroupEither = directory.groups.get(email)

    val groupEither = Either.fromTry(gGroupEither).map(gGroupToGroup)

    groupEither match {
      case Right(group) => Right[ErrorResponse, Group](group)
      case Left(e) => e.getLocalizedMessage match {
        case tooManyRequests  if tooManyRequests.contains("429")  => Left(TooManyRequests)
        case notFound         if notFound.contains("404")         => Left(NotFound)
        case conflict         if conflict.contains("409")         => Left(Conflict)
        case _                                                    => Left(InternalServerError)
      }
    }
  }

}
