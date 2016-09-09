package edu.eckerd.google.scgapi.services.auth

import akka.http.scaladsl.server.directives.Credentials
import scala.concurrent.Future
/**
  * Created by davenpcm on 9/9/16.
  */
trait AuthService {
  def authenticate(credentials: Credentials): Future[Option[String]]
}
