#!/usr/bin/groovy
package com.capgemini.productionline.templates;

class SonarQube implements Serializable {
    String username;
    String sonarQubeBaseUrl;

    /**
    * Initiate a new 'SonarQube' instance by providing a username and the SonarQube base URL.
    */
    SonarQube(String username, String sonarQubeBaseUrl)  {
        this.username = username;
        this.sonarQubeBaseUrl = sonarQubeBaseUrl;
    }

    /**
    * This method is using the provided username to request an API Token.
    */
    def getAuthToken() {
         println "output 1"
        URL sonarQubeTokenGenerateUrl = new URL(sonarQubeBaseUrl + "/api/user_tokens/generate")

        HttpURLConnection connection = (HttpURLConnection) sonarQubeTokenGenerateUrl.openConnection();
        connection.setDoOutput( true );
        connection.addRequestProperty("X-Forwarded-User", username)
        connection.addRequestProperty("X-Forwarded-Group", "sonarqube-admins")
        connection.addRequestProperty("Accept", "application/json")
        connection.setRequestMethod("POST")
        connection.connect()

        println "output"
        println connection.getResponseCode();
    }
}