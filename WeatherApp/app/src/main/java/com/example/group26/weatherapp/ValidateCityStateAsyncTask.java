package com.example.group26.weatherapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Carlos on 3/7/2016.
 */
public class ValidateCityStateAsyncTask extends AsyncTask<String, Void, Boolean> {

    IValidateCityStateAsyncTask activity;

    public ValidateCityStateAsyncTask(IValidateCityStateAsyncTask activity){
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.showProcessing();
    }

    @Override
    protected void onPostExecute(Boolean b) {
        super.onPostExecute(b);
        activity.finishProcessing();
        activity.setValidated(b);
    }

    @Override
    protected Boolean doInBackground(String... params) {

        try {
            // Make sure we handle spaces in the url correctly.
            String formatedURL = params[0];
            formatedURL = formatedURL.replace(" ", "%20");
            Log.d("testing", formatedURL);

            URL url = new URL(formatedURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int statusCode = connection.getResponseCode();

            if(statusCode == HttpURLConnection.HTTP_OK){
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = reader.readLine();

                while(line != null){
                    //Log.d("testing", line);
                    sb.append(line);
                    line = reader.readLine();
                }

                try {
                    String jsonString = sb.toString();
                    Log.d("testing", jsonString);

                    JSONObject locationAPIJsonObject = new JSONObject(jsonString);
                    String likelihood = locationAPIJsonObject.getString("likelihood");
                    String normalizedLocation = locationAPIJsonObject.getString("normalizedLocation");
                    String[] normalizedLocationArray = normalizedLocation.split(",");


                    /* Validate that the city/state combination is a legitimate/valid combination of city state in the United States. A good response looks like:

                    {
                        "status" : 200,
                            "requestId" : "aa1db8c9-caa6-4fd7-86f0-6053b3228a55",
                            "likelihood" : "1.0",
                            "city" : "Beacon",
                            "state" : {
                        "name" : "New York",
                                "code" : "NY"
                    },
                        "normalizedLocation" : "Beacon, New York"
                    }

                    However, "bad" or "non-valid" responses can look like:

                    {
                      "status" : 200,
                      "requestId" : "00703d5a-39b4-4c04-b60d-9549db995b2c",
                      "likelihood" : "0.5",
                      "city" : "Bacon",
                      "state" : {
                        "name" : "New York",
                        "code" : "NY"
                      },
                      "normalizedLocation" : "Bacon, New York"
                    }

                    Or they can look like:

                    {
                      "status" : 200,
                      "requestId" : "8f1a5974-fd13-4541-a990-ddf36ba2f97c",
                      "likelihood" : "0.5",
                      "city" : "Lol",
                      "state" : {
                        "name" : "New York",
                        "code" : "NY"
                      },
                      "normalizedLocation" : "Lol, New York"
                    }

                    */


                    if(likelihood.equals("1.0") && normalizedLocationArray.length == 2){
                        return true;
                    }
                    else {
                        return false;
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }

    public interface IValidateCityStateAsyncTask {
        void setValidated(boolean validated);
        void showProcessing();
        void finishProcessing();
    }
}
