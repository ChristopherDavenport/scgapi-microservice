package edu.eckerd.google.scgapi.persistence.google.core.users

import edu.eckerd.google.api.services.directory.models.{Email => gEmail, Name => gName, User => gUser}
import edu.eckerd.google.scgapi.models.User

/**
  * Created by davenpcm on 9/14/16.
  */
trait UserConversions {

  def gUserToUser(gUser: gUser): User = User(
    gUser.name.givenName,
    gUser.name.familyName,
    gUser.email.get,
    gUser.password,
    gUser.id,
    gUser.orgUnitPath,
    gUser.agreedToTerms,
    gUser.changePasswordAtNextLogin,
    gUser.includeInGlobalAddressList,
    gUser.ipWhiteListed,
    gUser.isAdmin,
    gUser.isMailboxSetup,
    gUser.suspended
  )

  def userToGUser(user: User): gUser = {
    val matchedUser = user.asMatchedUser

    gUser(
      gName(
        matchedUser.givenName,
        matchedUser.familyName
      ),
      gEmail(matchedUser.email),
      matchedUser.password,
      matchedUser.id,
      matchedUser.orgUnitPath,
      matchedUser.agreedToTerms,
      matchedUser.changePasswordAtNextLogin,
      matchedUser.includeInGlobalAddressList,
      matchedUser.ipWhiteListed,
      matchedUser.isAdmin,
      matchedUser.isMailboxSetup,
      matchedUser.isSuspended
    )
  }



}
