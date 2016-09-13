package edu.eckerd.google.scgapi.models

import enumeratum.EnumEntry
import enumeratum.Enum

/**
  * Created by davenpcm on 9/13/16.
  */
sealed trait MemberTypes extends EnumEntry

case object MemberTypes extends Enum[MemberTypes] {

  case object USER extends MemberTypes
  case object GROUP extends MemberTypes
  case object CUSTOMER extends MemberTypes

  val values = findValues

}