addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.8.1")
$if(DataflowFlexTemplate.truthy) $
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "1.1.0")
$endif$
