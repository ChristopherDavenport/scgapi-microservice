package edu.eckerd.google.scgapi.persistence.database

import java.sql.Timestamp

import edu.eckerd.google.api.services.directory.models.{Email, Name, User}


/**
  * Created by davenpcm on 9/8/16.
  */
trait GoogleTables {
  val profile : slick.driver.JdbcProfile
  import profile.api._

  lazy val schema = googleUsers.schema ++ googleGroups.schema ++ googleMembers.schema

  class GOOGLE_USERS(tag: Tag)  extends Table[User](tag, "GOOGLE_USERS") {
    def googleID = column[String]("GOOGLE_ID", O.PrimaryKey)
    def firstName = column[String]("FIRST_NAME")
    def lastName = column[String]("LAST_NAME")
    def email = column[String]("EMAIL")
    def isPrimaryEmail = column[Boolean]("IS_PRIMARY_EMAIL")
    def password = column[Option[String]]("PASSWORD") // Here Only For Matching
    def orgUnitPath = column[String]("ORG_UNIT_PATH")
    def agreedToTerms = column[Option[Boolean]]("AGREED_TO_TERMS")
    def changePasswordAtNextLogin = column[Boolean]("CHANGE_PASSWORD_NEXT_LOGIN")
    def includeInGlobalAddressList = column[Boolean]("INCLUDE_IN_GLOBAL_ADDRESS_LIST")
    def ipWhiteListed = column[Boolean]("IP_WHITELISTED")
    def isAdmin = column[Boolean]("IS_ADMIN")
    def isMailboxSetup = column[Boolean]("IS_MAILBOX_SETUP")
    def suspended = column[Boolean]("SUSPENDED")

    def * = (
      (
        firstName,
        lastName
      ),
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

  case class GoogleGroupsRow(id: String,
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
    def id = column[String]("ID", O.PrimaryKey)
    def autoIndicator = column[String]("AUTO_INDICATOR")
    def name = column[String]("NAME")
    def email = column[String]("EMAIL")
    def count = column[Long]("MEMBER_COUNT")
    def desc = column[Option[String]]("DESCRIPTION")
    def processIndicator = column[Option[String]]("PROCESS_INDICATOR")
    def autoType = column[Option[String]]("AUTO_TYPE")
    def autoKey = column[Option[String]]("AUTO_KEY")
    def autoTermCode = column[Option[String]]("AUTO_TERM_CODE")

    def pk = index("GROUP_GROUPS_PK", (id, autoType, autoKey), unique = true)

    def * = (id, autoIndicator , name, email, count, desc, processIndicator, autoType, autoKey, autoTermCode) <>
      (GoogleGroupsRow.tupled, GoogleGroupsRow.unapply)
  }

  lazy val googleGroups = new TableQuery(tag => new GOOGLE_GROUPS(tag))

  case class GoogleMembersRow(
                                   groupId: String,
                                   userID: String,
                                   userEmail: Option[String],
                                   autoIndicator: String,
                                   memberRole: String,
                                   memberType: String,
                                   processIndicator: Option[String] = None
                                 )

  class GOOGLE_MEMBERS(tag: Tag) extends Table[GoogleMembersRow](tag, "GOOGLE_MEMBERS") {
    def groupId = column[String]("GROUP_ID")
    def userID = column[String]("USER_ID")
    def userEmail = column[Option[String]]("USER_EMAIL")
    def autoIndicator = column[String]("AUTO_INDICATOR")
    def memberRole = column[String]("MEMBER_ROLE")
    def memberType = column[String]("MEMBER_TYPE")
    def processIndicator = column[Option[String]]("PROCESS_INDICATOR")

    def pk = index("GOOGLE_GROUP_TO_USER_PK", (groupId, userID), unique = true)
    def group = foreignKey("GROUP_FK", groupId, googleGroups)(_.id ,
      onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade
    )

    def * = (groupId, userID, userEmail, autoIndicator, memberRole, memberType, processIndicator) <>
      (GoogleMembersRow.tupled, GoogleMembersRow.unapply )
  }

  lazy val googleMembers = new TableQuery(tag => new GOOGLE_MEMBERS(tag))


//  case class GwbaliasRow(
//                          typePkCk: String,
//                          keyPk: String,
//                          alias: String,
//                          termCode: String,
//                          createGroupCk: String,
//                          createDate: Timestamp,
//                          activityDate: Timestamp,
//                          userId: String
//                        )

//  class GWBALIAS(tag: Tag) extends Table[GwbaliasRow](tag, "GWBALIAS") {
//    def typePkCk = column[String]("GWBALIAS_TYPE_PK_CK")
//    def keyPk = column[String]("GWBALIAS_KEY_PK")
//    def alias = column[String]("GWBALIAS_ALIAS")
//    def termCode = column[String]("GWBALIAS_TERM_CODE")
//    def createGroupCk = column[String]("GWBALIAS_CREATE_GROUP_CK")
//    def createDate =  column[Timestamp]("GWBALIAS_DATE_CREATED")
//    def activityDate = column[Timestamp]("GWBALIAS_ACTIVITY_DATE")
//    def userId= column[String]("GWBALIAS_USER_ID")
//
//    def * = (
//      typePkCk,
//      keyPk,
//      alias,
//      termCode,
//      createGroupCk,
//      createDate,
//      activityDate,
//      userId) <> (GwbaliasRow.tupled, GwbaliasRow.unapply)
//
//    def pk = primaryKey( "gwbalias_pk", (typePkCk, keyPk) )
//  }
//
//  lazy val gwbAlias = new TableQuery(tag => new GWBALIAS(tag))
}