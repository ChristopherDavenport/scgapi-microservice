package edu.eckerd.google.scgapi.models

import enumeratum._

sealed trait MemberRole extends EnumEntry

case object MemberRole extends Enum[MemberRole]{
  case object MEMBER extends MemberRole
  case object MANAGER extends MemberRole
  case object OWNER extends MemberRole

  val values = findValues

}
