
ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.7.2"

enablePlugins(ScalaNativePlugin)

Compile/mainClass := Some("team.starfish.lang.starLang")

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % "test"

lazy val root = (project in file("."))
  .settings(
    name := "starlang",
    scalacOptions += "-Yexplicit-nulls",
  )

import scala.scalanative.build._

nativeConfig ~= { c =>
  c.withLTO(LTO.none) // thin
    .withMode(Mode.debug) // releaseFast
    .withGC(GC.immix) // commix
}
