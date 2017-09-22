import sbtassembly.Log4j2MergeStrategy


name := "CuadreUpdate"

version := "0.1"

scalaVersion := "2.11.8"

lazy val slickVersion = "3.1.1"
lazy val akkaVersion = "2.4.10"

assemblyJarName in assembly := "cuadre-updater.jar"

resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/maven-releases/"

libraryDependencies ++= Seq(
  "com.typesafe.slick"  %% "slick"            % slickVersion,
  "com.typesafe.slick" %% "slick-extensions" % "3.1.0",
  "com.typesafe.slick"  %% "slick-hikaricp"   % slickVersion,
  "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
  "com.typesafe.akka"   %% "akka-actor"   % akkaVersion,
  "com.typesafe.akka"   %% "akka-stream"  % akkaVersion,
  "com.typesafe.akka"   %% "akka-slf4j"   % akkaVersion
)

assemblyMergeStrategy in assembly := {
  // Archivos en conflicto en las dependencias
  case PathList("META-INF", "org", "apache", "logging", "log4j", "core", "config", "plugins", "Log4j2Plugins.dat") => Log4j2MergeStrategy.plugincache
  case PathList("META-INF", "MTNMINDS.RSA") => MergeStrategy.first
  case PathList("javax", "ws", xs@_*) => MergeStrategy.first
  case PathList("javax", "mail", xs@_*) => MergeStrategy.first
  case PathList("javax", "annotation", xs@_*) => MergeStrategy.first
  case PathList("javax", "inject", xs@_*) => MergeStrategy.first
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

addCommandAlias("ensamblar", ";clean;update;compile;assembly")

mainClass in assembly := Some("Main")
