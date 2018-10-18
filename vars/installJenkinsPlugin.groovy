#!/usr/bin/env groovy

import jenkins.model.*
import java.util.logging.Logger

// Installs the given list of Jenkins plugins if not installed already. 
// Each entry needs to contain the plugin name and its version (e.g. git@2.0).
@NonCPS
def call(List<String> pluginsToInstall) {
  def logger = Logger.getLogger("")
  def newPluginInstalled = false
  def initialized = false
  def instance = Jenkins.getInstance()
  def pm = instance.getPluginManager()
  def uc = instance.getUpdateCenter()

  for (int i=0; i < pluginsToInstall.size(); i++) {
    def String pluginName = pluginsToInstall[i]

    stage("Installation of '$pluginName' Jenkins Plugin.") {
      // Install the plugin.
      if (!pm.getPlugin(pluginName)) {
        println "Plugin not installed yet - Searching '$pluginName' in the update center."
        // Check for updates.
        if (!initialized) {
          uc.updateAllSites()
          initialized = true
        }

        def plugin = uc.getPlugin(pluginName)
        if (plugin) {
          println "Installing $it Jenkins Plugin ..."

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
  if (newPluginInstalled) {
    println "Plugins installed, initializing a restart!"
    instance.save()
    instance.restart()
  }
}