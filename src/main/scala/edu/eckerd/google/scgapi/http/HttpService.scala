package edu.eckerd.google.scgapi.http
import akka.http.scaladsl.server.Route
/**
  * Created by davenpcm on 9/11/16.
  */
trait HttpService {
 val routes : Route
}
