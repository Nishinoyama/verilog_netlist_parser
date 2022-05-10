ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "2.1.1"

lazy val root = (project in file("."))
  .settings(
    name := "verilog_netlist_parser"
  )
