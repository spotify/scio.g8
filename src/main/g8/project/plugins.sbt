addDependencyTreePlugin
addSbtPlugin("com.here.platform" % "sbt-bom" % "1.0.16")
$if(DataflowFlexTemplate.truthy) $
addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.9.16")
$endif$
