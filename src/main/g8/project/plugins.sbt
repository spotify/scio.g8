addDependencyTreePlugin
addSbtPlugin("com.here.platform" % "sbt-bom" % "1.0.17")
$if(DataflowFlexTemplate.truthy) $
addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.10.4")
$endif$
