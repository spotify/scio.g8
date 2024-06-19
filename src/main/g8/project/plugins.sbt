addDependencyTreePlugin
$if(DataflowFlexTemplate.truthy) $
addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.9.16")
$endif$
