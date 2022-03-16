addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.9.9")
$if(DataflowFlexTemplate.truthy) $
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "1.1.0")
$endif$
