import sbt._
import Keys._

val scioVersion = "0.9.0"
val beamVersion = "2.20.0"
val scalaMacrosVersion = "2.1.1"
$if(FlinkRunner.truthy)$
val flinkVersion = "1.9.1"
$endif$
$if(SparkRunner.truthy)$
val sparkVersion = "2.4.4"
$endif$

lazy val commonSettings = Def.settings(
  organization := "$organization$",
  // Semantic versioning http://semver.org/
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.12.11",
  scalacOptions ++= Seq("-target:jvm-1.8",
                        "-deprecation",
                        "-feature",
                        "-unchecked"),
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8")
)

lazy val paradiseDependency =
  "org.scalamacros" % "paradise" % scalaMacrosVersion cross CrossVersion.full
lazy val macroSettings = Def.settings(
  libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  addCompilerPlugin(paradiseDependency)
)

lazy val root: Project = project
  .in(file("."))
  .settings(commonSettings)
  .settings(macroSettings)
  .settings(
    name := "$name;format="lower,hyphen"$",
    description := "$name$",
    publish / skip := true,
    run / classLoaderLayeringStrategy := ClassLoaderLayeringStrategy.Flat,
    libraryDependencies ++= Seq(
      "com.spotify" %% "scio-core" % scioVersion,
      "com.spotify" %% "scio-test" % scioVersion % Test,
      "org.apache.beam" % "beam-runners-direct-java" % beamVersion,
      $if(DataflowRunner.truthy)$
      "org.apache.beam" % "beam-runners-google-cloud-dataflow-java" % beamVersion,
      $endif$
      $if(FlinkRunner.truthy)$
      "org.apache.beam" % "beam-runners-flink-1.9" % beamVersion excludeAll (
        ExclusionRule("com.twitter", "chill_2.11"),
        ExclusionRule("org.apache.flink", "flink-clients_2.11"),
        ExclusionRule("org.apache.flink", "flink-runtime_2.11"),
        ExclusionRule("org.apache.flink", "flink-streaming-java_2.11")
      ),
      "org.apache.flink" %% "flink-clients" % flinkVersion,
      "org.apache.flink" %% "flink-runtime" % flinkVersion,
      "org.apache.flink" %% "flink-streaming-java" % flinkVersion,
      $endif$
      $if(SparkRunner.truthy)$
      "org.apache.beam" % "beam-runners-spark" % beamVersion exclude (
        "com.fasterxml.jackson.module", "jackson-module-scala_2.11"
      ),
      "org.apache.spark" %% "spark-core" % sparkVersion,
      "org.apache.spark" %% "spark-streaming" % sparkVersion,
      $endif$
      "org.slf4j" % "slf4j-simple" % "1.7.25"
    )
  )
  .enablePlugins(PackPlugin)

lazy val repl: Project = project
  .in(file(".repl"))
  .settings(commonSettings)
  .settings(macroSettings)
  .settings(
    name := "repl",
    description := "Scio REPL for $name$",
    libraryDependencies ++= Seq(
      "com.spotify" %% "scio-repl" % scioVersion
    ),
    Compile / mainClass := Some("com.spotify.scio.repl.ScioShell"),
    publish / skip := true
  )
  .dependsOn(root)
