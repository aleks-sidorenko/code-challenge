name := "spark-akka-http"

version := "1.0"

scalaVersion := "2.11.8"

organization := "com.interview"


libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.6",
  "com.typesafe.akka" %% "akka-stream" % "2.4.6",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.6",
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % "2.4.6",
  "com.typesafe.akka" %% "akka-http-testkit" % "2.4.6",
  "io.spray" %% "spray-can" % "1.3.4",
  "io.spray" %% "spray-http" % "1.3.4",
  "io.spray" %% "spray-client" % "1.3.4"
)
