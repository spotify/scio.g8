import sbt._
import Keys._
$if(DataflowFlexTemplate.truthy)$
import com.typesafe.sbt.packager.docker._
import scala.sys.process._
import complete.DefaultParsers._
$endif$

val scioVersion = "0.12.7"
val beamVersion = "2.38.0"
$if(FlinkRunner.truthy)$
val flinkVersion = "1.13.6"
$endif$
$if(SparkRunner.truthy)$
val sparkVersion = "3.1.2"
$endif$

lazy val commonSettings = Def.settings(
  organization := "$organization$",
  // Semantic versioning http://semver.org/
  version := "0.1.0-SNAPSHOT",
  $if(FlinkRunner.truthy || SparkRunner.truthy)$
  // scala-steward:off
  scalaVersion := "2.12.13",
  // scala-steward:on
  $else$
  scalaVersion := "2.13.3",
  $endif$
  $if(DataflowRunner.truthy)$
  resolvers += "confluent" at "https://packages.confluent.io/maven/",
  $endif$
  scalacOptions ++= Seq("-target:jvm-1.8",
                        "-deprecation",
                        "-feature",
                        "-unchecked",
                        $if(!FlinkRunner.truthy && !SparkRunner.truthy)$
                        "-Ymacro-annotations",
                        $endif$
                        ),
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8")
)

lazy val root: Project = project
  .in(file("."))
  .settings(commonSettings)
  $if(DataflowFlexTemplate.truthy)$
  .settings(assemblySettings)
  $endif$
  .settings(
    name := "$name;format="lower,hyphen"$",
    description := "$name$",
    publish / skip := true,
    run / classLoaderLayeringStrategy := ClassLoaderLayeringStrategy.Flat,
    run / fork := true,
    $if(FlinkRunner.truthy || SparkRunner.truthy)$
    addCompilerPlugin("org.scalamacros" % "paradise_2.12.13" % "2.1.1"),
    $endif$
    libraryDependencies ++= Seq(
      "com.spotify" %% "scio-core" % scioVersion,
      "com.spotify" %% "scio-test" % scioVersion % Test,
      "org.apache.beam" % "beam-runners-direct-java" % beamVersion,
      $if(DataflowRunner.truthy || DataflowFlexTemplate.truthy)$
      "org.apache.beam" % "beam-runners-google-cloud-dataflow-java" % beamVersion,
      $endif$
      $if(FlinkRunner.truthy)$
      "org.apache.beam" % "beam-runners-flink-1.13" % beamVersion excludeAll (
        ExclusionRule("com.twitter", "chill_2.11"),
        ExclusionRule("org.apache.flink", "flink-clients_2.11"),
        ExclusionRule("org.apache.flink", "flink-runtime_2.11"),
        ExclusionRule("org.apache.flink", "flink-streaming-java_2.11"),
        ExclusionRule("org.apache.flink", "flink-optimizer_2.11")
      ),
      "org.apache.flink" %% "flink-clients" % flinkVersion,
      "org.apache.flink" %% "flink-runtime" % flinkVersion,
      "org.apache.flink" %% "flink-streaming-java" % flinkVersion,
      "org.apache.flink" %% "flink-optimizer" % flinkVersion,
      $endif$
      $if(SparkRunner.truthy)$
      "org.apache.beam" % "beam-runners-spark" % beamVersion exclude (
        "com.fasterxml.jackson.module", "jackson-module-scala_2.11"
      ),
      "org.apache.spark" %% "spark-core" % sparkVersion,
      "org.apache.spark" %% "spark-streaming" % sparkVersion,
      $endif$
      "org.slf4j" % "slf4j-simple" % "2.0.9"
    )
  )
  .enablePlugins(JavaAppPackaging)

lazy val repl: Project = project
  .in(file(".repl"))
  .settings(commonSettings)
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

$if(DataflowFlexTemplate.truthy)$
lazy val gcpProject = settingKey[String]("GCP Project")
lazy val gcpRegion = settingKey[String]("GCP region")
lazy val createFlextTemplate = inputKey[Unit]("create DataflowFlexTemplate")
lazy val runFlextTemplate = inputKey[Unit]("run DataflowFlexTemplate")

lazy val assemblySettings = Def.settings(
  gcpProject := "",
  gcpRegion := "",
  assembly / test := {},
  assembly / assemblyJarName := "flex-wordcount.jar",
  assembly / assemblyMergeStrategy ~= { old =>
    {
      case s if s.endsWith(".properties") => MergeStrategy.filterDistinctLines
      case s if s.endsWith("public-suffix-list.txt") =>
        MergeStrategy.filterDistinctLines
      case s if s.endsWith(".class") => MergeStrategy.last
      case s if s.endsWith(".proto") => MergeStrategy.last
      case s if s.endsWith("reflection-config.json") => MergeStrategy.rename
      case s                         => old(s)
    }
  },
  Universal / mappings := {
    val fatJar = (Compile / assembly).value
    val filtered = (Universal / mappings).value.filter {
      case (_, name) => !name.endsWith(".jar")
    }
    filtered :+ (fatJar -> (s"lib/\${fatJar.getName}"))
  },
  scriptClasspath := Seq((assembly / assemblyJarName).value),
  Docker / packageName := s"gcr.io/\${gcpProject.value}/dataflow/templates/DataflowFlexTemplate",
  Docker / dockerCommands := Seq(
    Cmd(
      "FROM",
      "gcr.io/dataflow-templates-base/java11-template-launcher-base:latest"
    ),
    Cmd(
      "ENV",
      "FLEX_TEMPLATE_JAVA_MAIN_CLASS",
      (assembly / mainClass).value.getOrElse("")
    ),
    Cmd(
      "ENV",
      "FLEX_TEMPLATE_JAVA_CLASSPATH",
      s"/template/\${(assembly / assemblyJarName).value}"
    ),
    ExecCmd(
      "COPY",
      s"1/opt/docker/lib/\${(assembly / assemblyJarName).value}",
      "\${FLEX_TEMPLATE_JAVA_CLASSPATH}"
    )
  ),
  createFlextTemplate := {
    val _ = (Docker / publish).value
    s"""gcloud beta dataflow DataflowFlexTemplate build 
          gs://\${gcpProject.value}/dataflow/templates/\${name.value}.json
          --image \${dockerAlias.value}
          --sdk-language JAVA
          --metadata-file metadata.json""" !
  },
  runFlextTemplate := {
    val parameters = spaceDelimited("<arg>").parsed
    s"""gcloud beta dataflow DataflowFlexTemplate run \${name.value}
    	--template-file-gcs-location gs://\${gcpProject.value}/dataflow/templates/\${name.value}.json
    	--region=\${gcpRegion.value}
    	--parameters \${parameters.mkString(",")}""" !
  }
)
$endif$
