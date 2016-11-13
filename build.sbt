name := "toy-robot-simulator"

version := "1.0"

scalaVersion := "2.11.7"

assemblyJarName in assembly := "toy-robot-simulator.jar"
target in assembly := baseDirectory.value

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.9",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.9" % "test",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test"
)