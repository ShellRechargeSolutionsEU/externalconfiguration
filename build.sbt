import tnm.ScalaVersion.curr


val externalconfiguration = project.in(file("."))
  .enablePlugins(OssLibPlugin)
  .settings(
    organization := "com.thenewmotion",
    libraryDependencies ++= {
      val v = if (scalaVersion.value == curr) "2.6" else "2.5.1"
      Seq(
        "net.liftweb" %% "lift-util" % v,
        "javax.servlet" % "javax.servlet-api" % "3.0.1")
      .map(_ % "provided")
    })
