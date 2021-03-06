import sbt._
import Keys._

object ApplicationBuild extends Build {

  val appName         = "congremaps"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
     "com.github.mauricio" %% "postgresql-async" % "0.1.0",
     "org.jsoup" % "jsoup" % "1.7.2"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    scalaVersion := "2.10.1" 
  )

}
