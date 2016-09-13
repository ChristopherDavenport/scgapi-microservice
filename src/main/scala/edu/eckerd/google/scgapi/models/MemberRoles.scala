package edu.eckerd.google.scgapi.models

import enumeratum._

sealed trait MemberRoles extends EnumEntry

case object MemberRoles extends Enum[MemberRoles]{
  case object MEMBER extends MemberRoles
  case object MANAGER extends MemberRoles
  case object OWNER extends MemberRoles

  val values = findValues

}
