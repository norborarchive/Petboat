/*
 * RestClient.java
 * © 2011 nuboat.net. All rights reserved
 */
package net.nuboat.petboat.helper;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONObject;

/**
 *
 * @author  Peerapat Asoktummarungsri
 * @email   nuboat@gmail.com
 * @twitter @nuboat
 */
public class RestClient {

    private static final String TAG = "RestClient";

    private static final int HTTP_STATUS_OK = 200;

    private final String sUserAgent;

    private HttpClient client = null;

    public RestClient(String sUserAgent) {
        this.sUserAgent = sUserAgent;
        client = new DefaultHttpClient();
    }

    public JSONObject getJsonbyHttpGet(String url) throws Exception {
        return new JSONObject(httpGet(url));
    }

    private String httpGet(String url) throws Exception {
        HttpGet get = new HttpGet(url);
        get.setHeader("User-Agent", sUserAgent);

        HttpResponse response = client.execute(get);

        StatusLine status = response.getStatusLine();
        if (status.getStatusCode() != HTTP_STATUS_OK) {
            throw new Exception("Invalid response from server: " + status.toString()); }

        HttpEntity entity = response.getEntity();
        InputStream inputStream = entity.getContent();

        ByteArrayOutputStream content = new ByteArrayOutputStream();

        int readBytes = 0;
        byte[] sBuffer = new byte[512];
        while ((readBytes = inputStream.read(sBuffer)) != -1) {
            content.write(sBuffer, 0, readBytes); }

        return new String(content.toByteArray());
    }

}
