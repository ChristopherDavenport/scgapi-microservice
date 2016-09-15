package edu.eckerd.google.scgapi.models

/**
  * Created by Chris Davenport on 9/8/16.
  */
sealed trait User {
  val givenName : String
  val familyName: String
  val email: String

  def asMatchedUser : MatchedUser

}

object User {

  def apply(
             givenName: String,
             familyName: String,
             email: String,
             passwordOpt: Option[String],
             idOpt: Option[String],
             orgUnitPath: String,
             agreedToTermsOpt: Option[Boolean],
             changePasswordAtNextLogin : Boolean,
             includeInGlobalAddressList: Boolean,
             ipWhiteListed: Boolean,
             isAdmin: Boolean,
             isMailboxSetup: Boolean,
             isSuspended: Boolean) : User = {

    (passwordOpt, idOpt, agreedToTermsOpt) match {
      case (None, Some(id), Some(agreedToTerms)) =>
        CompleteUser(
          givenName, familyName, email,
          id, orgUnitPath,
          agreedToTerms, changePasswordAtNextLogin, includeInGlobalAddressList,
          ipWhiteListed, isAdmin, isMailboxSetup, isSuspended
        )
      case _ => MatchedUser(
        givenName, familyName, email,
        passwordOpt, idOpt, orgUnitPath,
        agreedToTermsOpt, changePasswordAtNextLogin, includeInGlobalAddressList,
        ipWhiteListed, isAdmin, isMailboxSetup, isSuspended
      )

    }

  }
}

final case class CompleteUser(
                       givenName: String,
                       familyName: String,
                       email: String,
                       id: String,
                       orgUnitPath: String,
                       agreedToTerms: Boolean,
                       changePasswordAtNextLogin: Boolean,
                       includeInGlobalAddressList: Boolean,
                       ipWhiteListed: Boolean,
                       isAdmin: Boolean,
                       isMailboxSetup: Boolean,
                       isSuspended: Boolean
                       ) extends User {
  def asMatchedUser: MatchedUser = {
    MatchedUser(
      givenName,
      familyName,
      email,
      None,
      Some(id),
      orgUnitPath,
      Some(agreedToTerms),
      changePasswordAtNextLogin,
      includeInGlobalAddressList,
      ipWhiteListed,
      isAdmin,
      isMailboxSetup,
      isSuspended
    )
  }
}

final case class MatchedUser(
                            givenName: String,
                            familyName: String,
                            email: String,
                            password: Option[String],
                            id: Option[String],
                            orgUnitPath: String,
                            agreedToTerms: Option[Boolean],
                            changePasswordAtNextLogin : Boolean,
                            includeInGlobalAddressList: Boolean,
                            ipWhiteListed: Boolean,
                            isAdmin: Boolean,
                            isMailboxSetup: Boolean,
                            isSuspended: Boolean
                            ) extends User {
  def asMatchedUser : MatchedUser = this
}


final case class UserBuilder(
                            givenName: String,
                            familyName: String,
                            email: String,
                            password: String,
                            orgUnit: Option[String],
                            agreedToTerms: Option[Boolean],
                            changePassWordAtNextLogin: Option[Boolean]
                            ) extends User {
  def asMatchedUser : MatchedUser = {

    val defaultOrgUnit = "/"
    val defaultAgreedToTerms = false
    val defaultChangePasswordAtNextLogin = false
    val defaultIncludeInGlobalAddressList = true
    val defaultIPWhitelisted = true
    val defaultIsAdmin = false
    val defaultIsMailboxSetup = false
    val defaultIsSuspended = false

    MatchedUser(
      givenName,
      familyName,
      email,
      Some(password),
      None,
      orgUnit.getOrElse(defaultOrgUnit),
      Some(agreedToTerms.getOrElse(defaultAgreedToTerms)),
      changePassWordAtNextLogin.getOrElse(defaultChangePasswordAtNextLogin),
      defaultIncludeInGlobalAddressList,
      defaultIPWhitelisted,
      defaultIsAdmin,
      defaultIsMailboxSetup,
      defaultIsSuspended
    )
  }
}
