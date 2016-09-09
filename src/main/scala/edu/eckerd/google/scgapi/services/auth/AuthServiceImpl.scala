package edu.eckerd.google.scgapi.services.auth

import akka.http.scaladsl.server.directives.Credentials

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by davenpcm on 9/9/16.
  */
class AuthServiceImpl(val httpAccessPassword: String)(implicit executionContext: ExecutionContext) extends AuthService {

  def authenticate(credentials: Credentials): Future[Option[String]] = credentials match {
    case p @ Credentials.Provided(id) =>
      Future {
        if (p.verify(httpAccessPassword)) Some(id)
        else None
      }
    case _ => Future.successful(None)
  }

}
