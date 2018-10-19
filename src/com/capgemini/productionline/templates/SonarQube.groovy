#!/usr/bin/groovy
package com.capgemini.productionline.templates;

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
        URL sonarQubeTokenGenerateUrl = new URL(sonarQubeBaseUrl + "/api/user_tokens/generate")

        HttpURLConnection connection = (HttpURLConnection) sonarQubeTokenGenerateUrl.openConnection();
        connection.setDoOutput( true );
        connection.addRequestProperty("X-Forwarded-User", username)
        connection.addRequestProperty("X-Forwarded-Group", "sonarqube-admins")
        connection.addRequestProperty("Accept", "application/json")
        connection.setRequestMethod("POST")

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name", tokenName));

        OutputStream outputStream = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(outputStream, "UTF-8"));
        writer.write(getQuery(params));
        writer.flush();
        writer.close();
        outputStream.close();
        
        connection.connect()

        // connection.getResponseCode()
        return connection.getResponseCode() + ":" + connection.getResponseMessage();
    }

    def importQualityProfile() {
        // TODO
    }
}