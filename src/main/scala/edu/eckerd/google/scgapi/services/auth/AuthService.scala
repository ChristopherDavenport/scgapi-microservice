package edu.eckerd.google.scgapi.services.auth

import akka.http.scaladsl.server.directives.Credentials
/**
  * Created by Chris Davenport on 9/9/16.
  */
trait AuthService {
  def authenticate(credentials: Credentials): Option[String]
}
