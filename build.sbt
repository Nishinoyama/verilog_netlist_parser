ThisBuild / version := "0.2.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "2.1.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.12" % Test

lazy val root = (project in file("."))
  .settings(
    name := "verilog_netlist_parser"
  )
