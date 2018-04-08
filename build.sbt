name := "chat"

version := "1.0"

lazy val `chat` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

scalaVersion := "2.12.2"

//unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

libraryDependencies ++= Seq(jdbc, ehcache, ws, specs2 % Test, guice)

libraryDependencies ++= Seq(
  "org.postgresql" % "postgresql" % "42.2.2",
  "org.jooq" % "jooq" % "3.10.5",
  "org.jooq" % "jooq-codegen-maven" % "3.10.5",
  "org.jooq" % "jooq-meta" % "3.10.5",

  "org.redisson" % "redisson" % "3.6.0",
  "com.fasterxml.jackson.core" % "jackson-core" % "2.9.4",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.4",

)

//val generateJOOQ = taskKey[Seq[File]]("Generate JooQ classes")
//
//val generateJOOQTask = (sourceManaged, fullClasspath in Compile, runner in Compile, streams) map { (src, cp, r, s) =>
//  toError(r.run("org.jooq.util.GenerationTool", cp.files, Array("conf/chatroom_jooq.xml"), s.log))
//  ((src / "main/generated") ** "*.scala").get
//}
//
//generateJOOQ <<= generateJOOQTask
//
//unmanagedSourceDirectories in Compile += sourceManaged.value / "main/generated"


// Enable the plugin
enablePlugins(JooqCodegen)

// jOOQ library version
// default: 3.10.1
jooqVersion := "3.10.5"

// jOOQ maven group ID
// If you want to use commercial version of jOOQ, set appropriate group ID
// For details, please refer to the jOOQ manual
// default: org.jooq
jooqGroupId := "org.jooq"

// Add jOOQ dependencies automatically if true
// If you want to manage jOOQ dependencies manually, set this flag to false
// default: true
autoJooqLibrary := true

// jOOQ codegen configuration file path
// required this or jooqCodegenConfig
// target directory will replace by jooqCodegenTargetDirectory
jooqCodegenConfigFile := Some(file("conf") / "chatroom_jooq.xml")

// generator target directory
// default: sourceManaged in Compile
// jooqCodegenTargetDirectory := file("path") / "to" / "target" / "directory"

// configuration file rewrite rules
// default: replace target directory
// jooqCodegenConfigRewriteRules += /* scala.xml.transform.RewriteRule */

// jOOQ codegen configuration
// required this or jooqCodegenConfigFile
// If setting, jooqCodegenConfigFile, jooqCodegenTargetDirectory and jooqCodegenConfigRewriteRules are ignored
// jooqCodegenConfig :=
//   <configuration>
//     <!-- configurations... -->
//   </configuration>

// codegen execution strategy
// default: CodegenStrategy.IfAbsent
// jooqCodegenStrategy := CodegenStrategy.Always
