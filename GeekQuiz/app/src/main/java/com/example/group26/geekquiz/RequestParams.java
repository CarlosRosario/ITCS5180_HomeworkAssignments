package com.example.group26.geekquiz;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Carlos on 2/20/2016.
 */
public class RequestParams {

    String method;
    String baseUrl;
    Map<String, String> params = new HashMap<String, String>();


    public RequestParams(String method, String baseUrl){
        this.method = method;
        this.baseUrl = baseUrl;
    }

    public void addParam(String key, String value){
        params.put(key,value);
    }

    public String getEncodedParams(){
        StringBuilder sb = new StringBuilder();

        for(String key : params.keySet()){
            try {
                String value = URLEncoder.encode(params.get(key), "UTF-8");

                if(sb.length() > 0){
                    sb.append("&");
                }
                sb.append(key + "=" + value);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public String getEncodedUrl(){
        String encodedUrl = this.baseUrl + "?" + getEncodedParams();
        Log.d("demo", encodedUrl);
        return encodedUrl;
    }

    public HttpURLConnection setupConnection()throws MalformedURLException, IOException {
        if(method.equals("POST")){
            URL url = new URL(this.baseUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(getEncodedParams());
            writer.flush();

            return connection;

        }
        else if (method.equals("GET")){
            URL url = new URL(getEncodedUrl());
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            Log.d("demo", "returning from setupConnection");
            return connection;

        }

        return null;
    }
}
