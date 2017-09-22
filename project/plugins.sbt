resolvers ++=
  Seq(
    "jgit-repo" at "http://download.eclipse.org/jgit/maven",
    Resolver.sonatypeRepo("public"),
    Classpaths.sbtPluginReleases
  )

resolvers += Resolver.url( "idio", url("http://dl.bintray.com/idio/sbt-plugins") )(Resolver.ivyStylePatterns)

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.13.0")
addSbtPlugin("org.idio" % "sbt-assembly-log4j2" % "0.1.0")