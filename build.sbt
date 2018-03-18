name := "chat"

version := "1.0"

lazy val `chat` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(jdbc, ehcache, ws, specs2 % Test, guice)

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

// https://mvnrepository.com/artifact/org.redisson/redisson
libraryDependencies += "org.redisson" % "redisson" % "3.6.0"

// https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-core" % "2.9.4"
// https://mvnrepository.com/artifact/com.fasterxml.jackson.module/jackson-module-scala
libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.4"


// https://mvnrepository.com/artifact/postgresql/postgresql
//libraryDependencies += "postgresql" % "postgresql" % "9.3-1100-jdbc41"

// https://mvnrepository.com/artifact/org.jooq/jooq
libraryDependencies ++= Seq(
  // https://mvnrepository.com/artifact/org.postgresql/postgresql
  "org.postgresql" % "postgresql" % "42.2.1",
  "org.jooq" % "jooq" % "3.10.5",
  "org.jooq" % "jooq-codegen-maven" % "3.10.5",
  "org.jooq" % "jooq-meta" % "3.10.5"
)

val generateJOOQ = taskKey[Seq[File]]("Generate JooQ classes")

val generateJOOQTask = (sourceManaged, fullClasspath in Compile, runner in Compile, streams) map { (src, cp, r, s) =>
  toError(r.run("org.jooq.util.GenerationTool", cp.files, Array("conf/chatroom_jooq.xml"), s.log))
  ((src / "main/generated") ** "*.scala").get
}

generateJOOQ <<= generateJOOQTask


unmanagedSourceDirectories in Compile += sourceManaged.value / "main/generated"
