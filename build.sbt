
lazy val buildSettings = Seq(
  name := "scgapi-microservice",
  organization := "edu.eckerd",
  version := "0.1.0-SNAPSHOT",
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
    List(
      "edu.eckerd" %% "google-api-scala" % "0.1.0",
      "com.typesafe.slick" %% "slick" % slickV,
      "com.typesafe.slick" %% "slick-extensions" % slickV ,
      "com.typesafe.slick" %% "slick-hikaricp" % slickV,
      "com.typesafe.akka" %% "akka-http-core" % akkaV,
      "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaV,
      "com.typesafe.akka" %% "akka-http-testkit" % akkaV % "test",
      "org.scalatest" %% "scalatest" % "3.0.0"
//      "io.monix" %% "monix" % "2.0.0",
//      "io.monix" %% "monix-cats" % "2.0.0"
//      "org.typelevel" %% "cats" % "0.7.2",
//      "com.h2database" % "h2" % "1.4.192"
    )
  }
)

lazy val scoverageSettings = Seq(
  coverageMinimum := 60,
  coverageFailOnMinimum := false,
  coverageExcludedFiles := ".*/src/test/.*"
)


lazy val coreSettings = buildSettings ++ commonSettings ++ dependencySettings ++ scoverageSettings

lazy val scgapi = project.in(file("."))
  .settings(coreSettings:_*)
