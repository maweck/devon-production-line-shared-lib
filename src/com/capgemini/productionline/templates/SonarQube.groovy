#!/usr/bin/groovy
package com.capgemini.productionline.templates;

import jenkins.plugins.http_request.*

class SonarQube implements Serializable {
    def String username;
    def String sonarQubeBaseUrl;

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
    def getAuthToken(String tokenName) {
        response = httpRequest consoleLogResponseBody: true, customHeaders: [[maskValue: true, name: 'X-Forwarded-User', value: username], [maskValue: true, name: 'X-Forwarded-Groups', value: 'admins']], httpMode: 'POST', url: 'http://sonarqube-core:9000/sonarqube/api/user_tokens/generate?name=' + tokenName
        parsedJsonResponse = readJSON text: response.getContent()

        // Return a token 
        return parsedJsonResponse.token
    }

    def importQualityProfile() {
        // TODO
    }
}