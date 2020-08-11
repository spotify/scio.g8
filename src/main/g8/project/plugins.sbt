addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.7.4")
$if(DataflowFlexTemplate.truthy) $
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.10")
$endif$
