package com.thenewmotion.externalconfiguration
package examples

/**
 * @author Yaroslav Klymko
 */
object ExternalConfigurationExample {
  // by default it will configure logback, typesafe config, lift props
  ExternalConfiguration.load()


  // if default options are not enough for you
  val myConfConfigurable: ExternalConfiguration.Configurable = propsDirPath => {
    val myConf = propsDirPath + "/myconf.myconf"
    // use myconf
  }

  ExternalConfiguration.load(myConfConfigurable :: ExternalConfiguration.defaultConfigurables)
}
