package edu.eckerd.google.scgapi.persistence.google.core.shared

import edu.eckerd.google.scgapi.models.ErrorResponse
import edu.eckerd.google.scgapi.models.ErrorResponse.{Conflict, InternalServerError, NotFound}

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

/**
  * Created by davenpcm on 9/14/16.
  */
trait GoogleErrorHandling {

  @tailrec
  final def handleGoogleErrors[A](f: => A): Either[ErrorResponse, A] = {
    Try(f) match {
      case Success(a) =>
        Right(a)
      case Failure(e) =>
        e.getLocalizedMessage match {
          case tooManyRequests  if tooManyRequests.contains("429")  =>
            Thread.sleep(100)
            handleGoogleErrors(f)
          case notFound         if notFound.contains("404")         => Left(NotFound)
          case conflict         if conflict.contains("409")         => Left(Conflict)
          case _                                                    => Left(InternalServerError)
        }
    }
  }

}
