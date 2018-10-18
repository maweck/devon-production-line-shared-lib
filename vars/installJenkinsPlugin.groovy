#!/usr/bin/env groovy

import jenkins.model.*
import java.util.logging.Logger

// Installs the given list of Jenkins plugins if not installed already. 
// Each entry needs to contain the plugin name and its version (e.g. git@2.0).
@NonCPS
def call(List<String> pluginsToInstall) {
  def logger = Logger.getLogger("")
  def installed = false
  def initialized = false
  def instance = Jenkins.getInstance()
  def pm = instance.getPluginManager()
  def uc = instance.getUpdateCenter()

  pluginsToInstall.each {
    stage("Installation of '${it}' Jenkins Plugin.") {
      // Install the plugin.

      if (!pm.getPlugin("${it}")) {
        println "Plugin not installed yet - Searching ${it} in the update center."
        // Check for updates.
        if (!initialized) {
          uc.updateAllSites()
          initialized = true
        }

        def plugin = uc.getPlugin("${it}")
        if (plugin) {
          println "Installing $it Jenkins Plugin ..."
          def installFuture = plugin.deploy()
          while(!installFuture.isDone()) {
            sleep(3000)
          }
          
          println "... Plugin has been installed"
        } else {
          println "Could not find the" + it + "Jenkins Plugin."
        }
      }
    }
    installed = true
  } 
  if (installed) {
    println "Plugins installed, initializing a restart!"
    instance.save()
    instance.restart()
  }
}