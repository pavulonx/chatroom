logLevel := Level.Info

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "org.postgresql" % "postgresql" % "42.2.2"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.12")

addSbtPlugin("com.github.kxbmap" % "sbt-jooq" % "0.3.3")
