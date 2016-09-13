package edu.eckerd.google.scgapi.services.core

/**
  * Created by Chris Davenport on 9/13/16.
  */
trait CoreFunctions {

  def emailParse(email: String): String = if (email.contains('@')) {email} else email + "@eckerd.edu"

}
