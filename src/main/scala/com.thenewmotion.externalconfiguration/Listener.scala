package com.thenewmotion.externalconfiguration

import javax.servlet.{ServletContextEvent, ServletContextListener}

/**
 * @author Yaroslav Klymko
 */
class Listener extends ServletContextListener {
  def contextDestroyed(sce: ServletContextEvent) {}

  def contextInitialized(sce: ServletContextEvent) {
    ExternalConfiguration.load()
  }
}