#!/usr/bin/env groovy

import jenkins.model.*
import java.util.logging.Logger

// Installs the given list of Jenkins plugins if not installed already. 
// Before installing a plugin, the UpdateCenter is updated.
//
// The method returns a boolean that describes if a new plugin has been installed or not. 
// Notice: If a plugin has been installed, a restart has to be initialized.
@NonCPS
def call(pluginsToInstall) {
  def logger = Logger.getLogger("")
  def newPluginInstalled = false
  def initialized = false
  def instance = Jenkins.getInstance()
  def pm = instance.getPluginManager()
  def uc = instance.getUpdateCenter()

  stage("Installation of Jenkins Plugins.") {
    pluginsToInstall.each {
      def String pluginName = it
      // Check if the plugin is already installed.
      if (!pm.getPlugin(pluginName)) {
        println "Plugin not installed yet - Searching '$pluginName' in the update center."
        // Check for updates.
        if (!initialized) {
          uc.updateAllSites()
          initialized = true
        }

        def plugin = uc.getPlugin(pluginName)
        if (plugin) {
          println "Installing '$pluginName' Jenkins Plugin ..."

          def installFuture = plugin.deploy()
          while(!installFuture.isDone()) {
            sleep(3000)
          }
          newPluginInstalled = true

          println "... Plugin has been installed"
        } else {
          println "Could not find the '$pluginName' Jenkins Plugin."
        }
      } else {
        println "The '$pluginName' Jenkins Plugin is already installed."
      }
    }
  }

  return newPluginInstalled
}