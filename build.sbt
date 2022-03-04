val scioVersion = "0.11.4"
val beamVersion = "2.37.0"
val flinkVersion = "1.13.6"
val sparkVersion = "3.2.1"

lazy val root = project
  .in(file("."))
  .enablePlugins(ScriptedPlugin)
  .settings(
    name := "scio.g8",
    Test / test := {
      val _ = (Test / g8Test).toTask("").value
    },
    scriptedLaunchOpts ++= List(
      "-Xms1024m",
      "-Xmx1024m",
      "-XX:ReservedCodeCacheSize=128m",
      "-XX:MaxPermSize=256m",
      "-Xss2m",
      "-Dfile.encoding=UTF-8"
    ),
    // Get scala-steward to update template dependencies
    libraryDependencies ++= Seq(
      "com.spotify" %% "scio-core" % scioVersion,
      "org.apache.beam" % "beam-runners-direct-java" % beamVersion,
      "org.apache.flink" %% "flink-runtime" % flinkVersion,
      "org.apache.spark" %% "spark-core" % sparkVersion,
      "org.slf4j" % "slf4j-simple" % "1.7.36"
    )
  )
