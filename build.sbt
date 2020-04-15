ThisBuild / organization := "io.kinoplan"

inThisBuild(
  List(
    homepage := Some(url("https://github.com/kinoplan")),
    licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT")),
    developers := List(
      Developer(
        "kinoplan",
        "Kinoplan",
        "job@kinoplan.ru",
        url("https://kinoplan.tech")
      )
    ),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/kinoplan"),
        "scm:git:git@github.com:kinoplan.git"
      )
    )
  )
)

lazy val root = project.in(file(".")).settings(BuildSettings.common).aggregate(
  core, `play-json`, circe, reactivemongo
).settings(
  name            := BuildSettings.projectName,
  skip in publish := true
)

lazy val core = project.in(file("core"))
  .settings(BuildSettings.common)
  .settings(name := s"${BuildSettings.projectName}-core")
  .settings(libraryDependencies ++= testDeps)

lazy val `play-json` = project.in(file("play-json"))
  .dependsOn(core)
  .settings(BuildSettings.common)
  .settings(name := s"${BuildSettings.projectName}-play-json")
  .settings(libraryDependencies ++= testDeps ++ Seq(
    "com.typesafe.play" %% "play-json" % "2.8.1" % Provided
  ))

lazy val circe = project.in(file("circe"))
  .dependsOn(core)
  .settings(BuildSettings.common)
  .settings(name := s"${BuildSettings.projectName}-circe")
  .settings(libraryDependencies ++= testDeps ++ Seq(
    "io.circe" %% "circe-core" % "0.13.0" % Provided
  ))

lazy val reactivemongo = project.in(file("reactivemongo"))
  .dependsOn(core)
  .settings(BuildSettings.common)
  .settings(name := s"${BuildSettings.projectName}-reactivemongo")
  .settings(libraryDependencies ++= testDeps ++ Seq(
    "org.reactivemongo" %% "reactivemongo" % "0.18.1"
  ))

lazy val testDeps = Seq(
  "org.scalatest" %% "scalatest" % "3.1.1" % Test,
  "org.scalacheck" %% "scalacheck" % "1.14.1" % Test,
  "org.scalatestplus" %% "scalacheck-1-14" % "3.1.1.1" % Test
)
