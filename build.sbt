enablePlugins(JavaServerAppPackaging, RpmPlugin, SystemdPlugin)

lazy val buildSettings = Seq(
  name := "scgapi-microservice",
  organization := "edu.eckerd",
  version := "0.0.1-SNAPSHOT",
  scalaVersion := "2.11.8"
)

lazy val commonSettings = Seq(
  scalacOptions := Seq(
    "-feature",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-Xfatal-warnings",
    "-deprecation",
    "-unchecked"
  ),
  resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots"),
    "Typesafe Releases" at "http://repo.typesafe.com/typesafe/maven-releases/"
  ),
  scalacOptions in console in Compile -= "-Xfatal-warnings",
  scalacOptions in console in Test -= "-Xfatal-warnings"
)

lazy val dependencySettings = Seq(
  libraryDependencies ++= {
    val slickV = "3.1.0"
    val akkaV = "2.4.10"
    val enumeratumV = "1.4.14"
    List(
      "edu.eckerd" %% "google-api-scala" % "0.1.0",
      "com.typesafe.slick" %% "slick" % slickV,
      "com.typesafe.slick" %% "slick-extensions" % slickV ,
      "com.typesafe.slick" %% "slick-hikaricp" % slickV,
      "com.typesafe.akka" %% "akka-http-core" % akkaV,
      "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaV,
      "com.typesafe.akka" %% "akka-http-testkit" % akkaV % "test",
      "org.scalatest" %% "scalatest" % "3.0.0",
      "com.h2database" % "h2" % "1.4.192",
      "ch.qos.logback" %  "logback-classic" % "1.1.7",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.4.0",
//      "io.monix" %% "monix" % "2.0.0"
//      "io.monix" %% "monix-cats" % "2.0.0"
      "org.typelevel" %% "cats" % "0.7.2",
      "com.beachape" %% "enumeratum" % enumeratumV
    )
  }
)

lazy val scoverageSettings = Seq(
  coverageMinimum := 60,
  coverageFailOnMinimum := false,
  coverageExcludedFiles := ".*/src/test/.*"
)

lazy val appPackagerSettings = Seq(
  name in Linux := name.value,
  maintainer := "Christopher Davenport <ChristopherDavenport@outlook.com>",
  packageSummary := "Google Organization RESTful Controls",
  packageDescription :=
    """This application takes simple config values and allows you to leverage control over your
      |google organization.
    """.stripMargin,
  packageArchitecture in Linux := "x86_64",
  version in Linux := "0.0.1",
  rpmRelease := "1",
  rpmVendor := "eckerd",
  rpmUrl := Some("https://github.com/ChristopherDavenport/scgapi-microservice"),
  rpmLicense := Some("Apache 2.0"),
  daemonUser in Rpm := "scgapi",
  linuxPackageMappings in Rpm := linuxPackageMappings.value,
  packageArchitecture in Rpm := "x86_64",
  defaultLinuxStartScriptLocation in Debian := "/usr/lib/systemd/system",
  rpmRequirements := Seq("java"),
  defaultLinuxInstallLocation := "/opt/package_root",
  rpmPrefix := Some(defaultLinuxInstallLocation.value),
  linuxPackageSymlinks := Seq.empty,
  defaultLinuxLogsLocation := defaultLinuxInstallLocation + "/" + name

)


lazy val coreSettings = buildSettings ++ commonSettings ++ dependencySettings ++ scoverageSettings

lazy val scgapi = project.in(file("."))
  .settings(coreSettings:_*)
  .settings(appPackagerSettings:_*)

