package edu.eckerd.google.scgapi.models

import enumeratum.{EnumEntry, Enum}

/**
  * Created by davenpcm on 9/14/16.
  */
sealed abstract class ErrorResponse(val errorEntry: ErrorEntry) extends EnumEntry

final case class ErrorEntry(code: Int, message: String)

case object ErrorResponse extends Enum[ErrorResponse]{

  // 400 Client Errors
//  case object BadRequest extends ErrorResponse(400, The request could not be understood by the server due to malformed syntax.)
//  case object Unauthorized extends ErrorResponse
//  case object Forbidden extends ErrorResponse
  case object NotFound extends ErrorResponse(ErrorEntry(404, "Entity could not be found."))
  case object Conflict extends ErrorResponse(ErrorEntry(409, "Entity already exists."))

  case object TooManyRequests extends ErrorResponse(ErrorEntry(429, "The user has sent too many requests in a given amount of time."))

  // 500 Server Errors
  case object InternalServerError extends ErrorResponse(ErrorEntry(500, "An Unknown Error has occurred, please try again later."))

  val values = findValues
}
