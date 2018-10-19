#!/usr/bin/groovy
package com.capgemini.productionline.templates;

import org.apache.commons.httpclient.NameValuePair 

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
        return httpRequest consoleLogResponseBody: true, customHeaders: [[maskValue: true, name: 'X-Forwarded-User', value: username], [maskValue: true, name: 'X-Forwarded-Groups', value: 'admins']], httpMode: 'POST', url: 'http://sonarqube-core:9000/sonarqube/api/user_tokens/generate?name=' + tokenName
    }

    def importQualityProfile() {
        // TODO
    }
}