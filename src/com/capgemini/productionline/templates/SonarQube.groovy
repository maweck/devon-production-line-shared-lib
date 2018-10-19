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
        //URL sonarQubeTokenGenerateUrl = new URL(sonarQubeBaseUrl + "/api/user_tokens/generate")
        URL sonarQubeTokenGenerateUrl = new URL(sonarQubeBaseUrl + "api/user_tokens/search")
        

        HttpURLConnection connection = (HttpURLConnection) sonarQubeTokenGenerateUrl.openConnection();
        connection.setDoOutput( true );
        connection.addRequestProperty("X-Forwarded-User", username)
        connection.addRequestProperty("X-Forwarded-Group", "sonarqube-admins")
        connection.addRequestProperty("Accept", "application/json")
        connection.setRequestMethod("POST")

        OutputStream outputStream = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(outputStream, "UTF-8"));
        writer.write("name="+tokenName);
        writer.flush();
        writer.close();
        outputStream.close();
        
        connection.connect()
        
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();

        // connection.getResponseCode()
        return connection.getResponseCode() + ":" + connection.getResponseMessage() + ":" +  connection.getContent() + ":" + sb.toString();
    }

    def importQualityProfile() {
        // TODO
    }
}