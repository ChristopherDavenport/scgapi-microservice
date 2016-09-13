package edu.eckerd.google.scgapi.persistence.database

import edu.eckerd.google.api.services.directory.models.{Email, Name, User}

/**
  * Created by Chris Davenport on 9/8/16.
  */
trait GoogleTables {
  val profile : slick.driver.JdbcProfile
  import profile.api._

  lazy val schema = googleUsers.schema ++ googleGroups.schema ++ googleMembers.schema

  class GOOGLE_USERS(tag: Tag) extends Table[User](tag, "GOOGLE_USERS") {
    def googleID                    : Rep[String]           = column[String]("GOOGLE_ID", O.PrimaryKey)
    def firstName                   : Rep[String]           = column[String]("FIRST_NAME")
    def lastName                    : Rep[String]           = column[String]("LAST_NAME")
    def email                       : Rep[String]           = column[String]("EMAIL")
    def password                    : Rep[Option[String]]   = column[Option[String]]("PASSWORD")
    def orgUnitPath                 : Rep[String]           = column[String]("ORG_UNIT_PATH")
    def agreedToTerms               : Rep[Option[Boolean]]  = column[Option[Boolean]]("AGREED_TO_TERMS")
    def changePasswordAtNextLogin   : Rep[Boolean]          = column[Boolean]("CHANGE_PASSWORD_NEXT_LOGIN")
    def includeInGlobalAddressList  : Rep[Boolean]          = column[Boolean]("INCLUDE_IN_GLOBAL_ADDRESS_LIST")
    def ipWhiteListed               : Rep[Boolean]          = column[Boolean]("IP_WHITELISTED")
    def isAdmin                     : Rep[Boolean]          = column[Boolean]("IS_ADMIN")
    def isMailboxSetup              : Rep[Boolean]          = column[Boolean]("IS_MAILBOX_SETUP")
    def suspended                   : Rep[Boolean]          = column[Boolean]("SUSPENDED")

    def * = (
      // Name Case Class
      (
        firstName,
        lastName
      ),
      // Email Case Class Single Parameter
      email,
      password,
      googleID,
      orgUnitPath,
      agreedToTerms,
      changePasswordAtNextLogin,
      includeInGlobalAddressList,
      ipWhiteListed,
      isAdmin,
      isMailboxSetup,
      suspended
    ).shaped <> (
      {
        case (
          name,
          email,
          password,
          id,
          orgUnitPath,
          agreedToTerms,
          changePasswordAtNextLogin,
          includeInGlobalAddressList,
          ipWhiteListed,
          isAdmin,
          isMailboxSetup,
          suspended
          ) =>
          User(
            Name.tupled.apply(name),
            Email(email),
            password,
            Some(id),
            orgUnitPath,
            agreedToTerms,
            changePasswordAtNextLogin,
            includeInGlobalAddressList,
            ipWhiteListed,
            isAdmin,
            isMailboxSetup,
            suspended
          )
      },
      {
        i : User =>
          def f1(p: Name) = Name.unapply(p).get
          Some((
            f1(i.name),
            i.primaryEmail.address,
            i.password,
            i.id.getOrElse("BAD ID"),
            i.orgUnitPath,
            i.agreedToTerms,
            i.changePasswordAtNextLogin,
            i.includeInGlobalAddressList,
            i.ipWhiteListed,
            i.isAdmin,
            i.isMailboxSetup,
            i.suspended
          ))
      }
    )
  }

  lazy val googleUsers = new TableQuery(tag => new GOOGLE_USERS(tag))

  case class GoogleGroupsRow(
                              id: String,
                              autoIndicator: String,
                              name: String,
                              email: String,
                              count: Long,
                              desc: Option[String],
                              processIndicator: Option[String] = None,
                              autoType: Option[String] = None,
                              autoKey: Option[String] = None,
                              autoTermCode : Option[String] = None
                            )

  class GOOGLE_GROUPS(tag: Tag) extends Table[GoogleGroupsRow](tag, "GOOGLE_GROUPS"){
    def id                : Rep[String]         = column[String]("ID", O.PrimaryKey)
    def autoIndicator     : Rep[String]         = column[String]("AUTO_INDICATOR")
    def name              : Rep[String]         = column[String]("NAME")
    def email             : Rep[String]         = column[String]("EMAIL")
    def count             : Rep[Long]           = column[Long]("MEMBER_COUNT")
    def desc              : Rep[Option[String]] = column[Option[String]]("DESCRIPTION")
    def processIndicator  : Rep[Option[String]] = column[Option[String]]("PROCESS_INDICATOR")
    def autoType          : Rep[Option[String]] = column[Option[String]]("AUTO_TYPE")
    def autoKey           : Rep[Option[String]] = column[Option[String]]("AUTO_KEY")
    def autoTermCode      : Rep[Option[String]] = column[Option[String]]("AUTO_TERM_CODE")

    def pk = index("GROUP_GROUPS_PK", (id, autoType, autoKey), unique = true)

    def * =
      (id, autoIndicator , name, email, count, desc, processIndicator, autoType, autoKey, autoTermCode) <>
      (GoogleGroupsRow.tupled, GoogleGroupsRow.unapply)
  }

  lazy val googleGroups = new TableQuery(tag => new GOOGLE_GROUPS(tag))

  case class GoogleMembersRow(
                               groupId: String,
                               userID: String,
                               userEmail: Option[String],
                               autoIndicator: String,
                               memberRole: String,
                               memberType: String
                             )

  class GOOGLE_MEMBERS(tag: Tag) extends Table[GoogleMembersRow](tag, "GOOGLE_MEMBERS") {
    def groupId       : Rep[String]         = column[String]("GROUP_ID")
    def userID        : Rep[String]         = column[String]("USER_ID")
    def userEmail     : Rep[Option[String]] = column[Option[String]]("USER_EMAIL")
    def autoIndicator : Rep[String]         = column[String]("AUTO_INDICATOR")
    def memberRole    : Rep[String]         = column[String]("MEMBER_ROLE")
    def memberType    : Rep[String]         = column[String]("MEMBER_TYPE")

    def pk = index("GOOGLE_GROUP_TO_USER_PK", (groupId, userID), unique = true)
    def group = foreignKey("GROUP_FK", groupId, googleGroups)(_.id ,
      onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade
    )

    override def * =
      (groupId, userID, userEmail, autoIndicator, memberRole, memberType) <>
      (GoogleMembersRow.tupled, GoogleMembersRow.unapply )
  }

  lazy val googleMembers = new TableQuery(tag => new GOOGLE_MEMBERS(tag))
}