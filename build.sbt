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
