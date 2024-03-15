// see https://github.com/spotify/scio/blob/v0.14.2/build.sbt
val scioVersion = "0.14.2"
val beamVersion = "2.54.0"
val slf4jVersion = "1.7.30"
val flinkVersion = "1.16.0"
val sparkVersion = "3.5.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scio.g8",
    Test / test := {
      val _ = (Test / g8Test).toTask("").value
    },
    Test / g8 / g8Properties ++= Map(
      "DataflowRunner" -> "yes",
      "FlinkRunner" -> "yes",
      "SparkRunner" -> "yes",
      "DataflowFlexTemplate" -> "yes"
    ),
    scriptedLaunchOpts ++= List(
      "-Xms1G",
      "-Xmx4G",
      "-Dfile.encoding=UTF-8"
    ),
    scriptedBufferLog := false,
  )
