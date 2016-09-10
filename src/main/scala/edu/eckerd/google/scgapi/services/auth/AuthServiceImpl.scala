package edu.eckerd.google.scgapi.services.auth

import akka.http.scaladsl.server.directives.Credentials
import akka.http.scaladsl.server.directives._
import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by davenpcm on 9/9/16.
  */
class AuthServiceImpl(private val httpAccessPassword: String) extends AuthService {

  def authenticate(credentials: Credentials): Option[String] = credentials match {
    case p @ Credentials.Provided(id) =>
        if (p.verify(httpAccessPassword)) Some(id)
        else None
    case _ => None
  }

}

object AuthServiceImpl{
  def apply(password: String): AuthServiceImpl = new AuthServiceImpl(password)
}
