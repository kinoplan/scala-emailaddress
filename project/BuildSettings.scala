import sbt.Keys._
import sbt._
import xerial.sbt.Sonatype.autoImport._

object BuildSettings {
  val projectName = "emailaddress"

  val common = Seq(
    resolvers += Resolver.sonatypeRepo("snapshots"),

    scalaVersion := "2.13.1",
    crossScalaVersions := Seq("2.12.10", scalaVersion.value),

    organization := "io.kinoplan",

    version := "0.1.0",

    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false },

    homepage := Some(url("https://github.com/tochkak")),
    licenses := Seq("Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    publishTo in ThisBuild := sonatypePublishToBundle.value,

    scalacOptions ++= Seq(
      "-language:higherKinds",
      "-language:implicitConversions",
      "-language:reflectiveCalls",
      "-language:existentials",
      "-language:postfixOps",
      "-Ywarn-unused",
      "-Ywarn-dead-code",
      "-Yrangepos",
      "-deprecation",
      "-feature",
      "-unchecked",
      "-encoding", "utf8"
    )
  )
}
