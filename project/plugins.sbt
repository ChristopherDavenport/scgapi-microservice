resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
logLevel := Level.Warn
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.3.5")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.2.0-M5")