package com.thenewmotion.externalconfiguration

import scala.util.{Try, Properties}
import java.io.{FileInputStream, File}

/**
 * @author Yaroslav Klymko
 */
object ExternalConfiguration {
  type PropsDir = String
  type Configurable = PropsDir => Any

  val propsDirName = "props.dir"

  /**
   * Should be called prior any other application calls
   */
  def load(configurables: List[Configurable] = defaultConfigurables) {
    def propsDirIn(key: String, value: String => Option[String]) = value(key).map {
      path =>
        val propsSuffix = "/props"
        val propsDir = path + propsSuffix
        warn(s"$propsDirName is not configured, using {$key}$propsSuffix: $propsDir")
        propsDir
    }

    def propsDir = Properties.propOrNone(propsDirName)
    def catalinaHomeEnv = propsDirIn("CATALINA_HOME", Properties.envOrNone)
    def catalinaHomeSys = propsDirIn("catalina.home", Properties.propOrNone)

    propsDir orElse catalinaHomeEnv orElse catalinaHomeSys match {
      case Some(dir) => load(dir, configurables)
      case None => warn(s"$propsDirName is not configured, make sure you started application with -D$propsDirName=<valid dir>")
    }
  }

  val defaultConfigurables: List[Configurable] = List(liftPropsConfigurable, configConfigurable, logbackConfigurable)

  def load(propsDirPath: String, configurables: List[Configurable]) {
    val file = new File(propsDirPath)
    if (!file.exists()) warn(s"$propsDirName is pointing to non existing dir: $propsDirPath")
    else if (!file.isDirectory) warn(s"$propsDirName should point do dir, not a file, path: $propsDirPath")
    else {
      info(s"loading configuration from $propsDirPath")
      configurables.foreach(_ apply propsDirPath)
    }
  }


  def liftPropsConfigurable(propsDirPath: String) {
    if (onClassPath("net.liftweb.util.Props")) {
      import net.liftweb.util.Props
      import net.liftweb.common.Full
      fileOrWarn(new File(propsDirPath + "/custom.props"), "PROPS").foreach {
        file =>
          Props.whereToLook = () => List(file.getName -> (() => Full(new FileInputStream(file))))
      }
    }
  }

  def configConfigurable(propsDirPath: String) {
    if (onClassPath("com.typesafe.config.Config")) {
      fileOrWarn(new File(propsDirPath + "/application.conf"), "CONFIG").foreach {
        file => Properties.setProp("config.file", file.getPath)
      }
    }
  }

  def logbackConfigurable(propsDirPath: String) {
    fileOrWarn(new File(propsDirPath + "/logback.xml"), "LOGBACK").foreach {
      file => Properties.setProp("logback.configurationFile", file.getPath)
    }
  }

  def fileOrWarn(file: File, label: String): Option[File] = {
    if (file.exists() && file.isFile && file.canRead) Some(file)
    else {
      if (!file.exists()) warn(s"$label: $file does not exist")
      else if (!file.isFile) warn(s"$label: $file is not a file")
      else if (!file.canRead) warn(s"$label: Can't read $file")
      None
    }
  }

  def onClassPath(className: String): Boolean = Try(() => getClass.getClassLoader.loadClass(className)).isSuccess

  def error(msg: String) { println(s"ERROR: $msg") }
  def info(msg: String) { println(s"INFO: $msg") }
  def warn(msg: String) { println(s"WARN: $msg") }
}