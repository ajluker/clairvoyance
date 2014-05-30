name := "clairvoyance"

organization := "com.github.rhyskeepence"

version := "33"

scalaVersion := "2.10.4"

libraryDependencies <<= scalaVersion { scala_version => Seq(
  "org.specs2" %% "specs2" % "2.3.12",
  "org.scalatest" %% "scalatest" % "2.2.0-RC1",
  "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.3",
  "org.pegdown" % "pegdown" % "1.4.2",
  "net.sourceforge.plantuml" % "plantuml" % "7999",
  "org.scalacheck" %% "scalacheck" % "1.11.4" % "optional",
  "org.scala-lang" % "scala-compiler" % scala_version  % "optional"
)}

resolvers ++= Seq(
  "sonatype releases"  at "http://oss.sonatype.org/content/repositories/releases"
)

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-language:implicitConversions,reflectiveCalls,postfixOps,higherKinds,existentials")

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-C", "clairvoyance.scalatest.export.ScalaTestHtmlReporter")

publishTo <<= version { v: String =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra :=
  <url>http://www.github.com/rhyskeepence/clairvoyance</url>
  <licenses>
    <license>
      <name>BSD-style</name>
      <url>http://www.opensource.org/licenses/bsd-license.php</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git://github.com/rhyskeepence/clairvoyance.git</url>
    <connection>scm:git://github.com/rhyskeepence/clairvoyance.git</connection>
  </scm>
  <developers>
    <developer>
      <id>rhyskeepence</id>
      <name>Rhys Keepence</name>
      <url>http://rhyskeepence.github.com</url>
    </developer>
    <developer>
      <id>franckrasolo</id>
      <name>Franck Rasolo</name>
      <url>https://github.com/franckrasolo</url>
    </developer>
  </developers>
