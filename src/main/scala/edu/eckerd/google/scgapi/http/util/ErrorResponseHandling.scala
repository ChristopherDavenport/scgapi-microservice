package edu.eckerd.google.scgapi.http.util

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import edu.eckerd.google.scgapi.models.{ErrorResponse, Message}
import edu.eckerd.google.scgapi.models.ErrorResponse._
import spray.json.JsonFormat

import scala.util.{Success, Try, Failure}

/**
  * Created by davenpcm on 9/14/16.
  */
trait ErrorResponseHandling {
  this : JsonProtocol =>

  def toRoute[A : JsonFormat](either: Either[ErrorResponse, A])(f: A => Route): Route = either match {
    case Right(a) => f(a)
    case Left(errorResponse) => errorResponse match {
      case NotFound => complete(StatusCodes.NotFound, NotFound.errorEntry)
      case Conflict => complete(StatusCodes.Conflict, Conflict.errorEntry)
      case TooManyRequests => complete(StatusCodes.TooManyRequests, TooManyRequests.errorEntry)
      case InternalServerError => complete(StatusCodes.InternalServerError, InternalServerError.errorEntry)
    }
  }

//  def toRoute[A : JsonFormat](tryVal: Try[Either[ErrorResponse, A]])(f: A => Route): Route = tryVal match {
//    case Success(either) => either match {
//      case Right(a) => f(a)
//      case Left(errorResponse) => errorResponse match {
//        case NotFound => complete(StatusCodes.NotFound, NotFound.errorEntry)
//        case Conflict => complete(StatusCodes.Conflict, Conflict.errorEntry)
//        case TooManyRequests => complete(StatusCodes.TooManyRequests, TooManyRequests.errorEntry)
//        case InternalServerError => complete(StatusCodes.InternalServerError, InternalServerError.errorEntry)
//      }
//    }
//    case _ => complete(StatusCodes.InternalServerError, InternalServerError.errorEntry)
//  }

}
