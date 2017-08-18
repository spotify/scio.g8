import sbt._
import Keys._

val scioVersion = "0.3.5"
val scalaMacrosVersion = "2.1.0"

lazy val commonSettings = Defaults.coreDefaultSettings ++ Seq(
  organization          := "$organization$",
  // Semantic versioning http://semver.org/
  version               := "0.1.0-SNAPSHOT",
  scalaVersion          := "2.11.11",
  scalacOptions         ++= Seq("-target:jvm-1.8",
                                "-deprecation",
                                "-feature",
                                "-unchecked"),
  javacOptions          ++= Seq("-source", "1.8",
                                "-target", "1.8")
)

lazy val paradiseDependency =
  "org.scalamacros" % "paradise" % scalaMacrosVersion cross CrossVersion.full
lazy val macroSettings = Seq(
  libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  addCompilerPlugin(paradiseDependency)
)

lazy val noPublishSettings = Seq(
  publish := {},
  publishLocal := {},
  publishArtifact := false
)

lazy val root: Project = Project(
  "$name;format="lower,hyphen"$",
  file("."),
  settings = commonSettings ++ macroSettings ++ noPublishSettings ++ Seq(
    description := "$name$",
    libraryDependencies ++= Seq(
      "com.spotify" %% "scio-core" % scioVersion,
      "org.slf4j" % "slf4j-simple" % "1.7.25",
      "com.spotify" %% "scio-test" % scioVersion % "test"
    )
  )
).enablePlugins(PackPlugin)

lazy val repl: Project = Project(
  "repl",
  file(".repl"),
  settings = commonSettings ++ macroSettings ++ noPublishSettings ++ Seq(
    description := "Scio REPL for $name$",
    libraryDependencies ++= Seq(
      "com.spotify" %% "scio-repl" % scioVersion
    ),
    mainClass in Compile := Some("com.spotify.scio.repl.ScioShell")
  )
).dependsOn(
  root
)
