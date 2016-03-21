package com.example.group26.async;

import android.os.AsyncTask;
import android.util.Log;

import com.example.group26.weatherapp.ValidateWithTemperature;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Carlos on 3/7/2016.
 */
public class ValidateCityStateAsyncTask extends AsyncTask<String, Void, ValidateWithTemperature> {

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
    protected void onPostExecute(ValidateWithTemperature b) {
        super.onPostExecute(b);
        activity.finishProcessing();
        activity.setValidated(b);
    }

    @Override
    protected ValidateWithTemperature doInBackground(String... params) {

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

                    connection.disconnect();
                    String temperature = "N/A";
                    if(likelihood.equals("1.0") && normalizedLocationArray.length == 2){

                        // Get the current temperature for this city now that we know it is a legitimate city

                        try{

                            // Make sure we handle spaces in the url correctly.
                            String formatedConditionsURL = params[1];
                            formatedConditionsURL = formatedConditionsURL.replace(" ", "%20");
                            Log.d("testing", formatedConditionsURL);
                            URL conditionsUrl = new URL(formatedConditionsURL);

                            //connection
                            HttpURLConnection secondConnection = (HttpURLConnection)conditionsUrl.openConnection();
                            secondConnection.setRequestMethod("GET");
                            secondConnection.connect();
                            statusCode = secondConnection.getResponseCode();

                            if(statusCode == HttpURLConnection.HTTP_OK){
                                InputStream in = secondConnection.getInputStream();

//                                BufferedReader reader2 = new BufferedReader(new InputStreamReader(secondConnection.getInputStream()));
//                                StringBuilder sb2 = new StringBuilder();
//                                String line2 = reader2.readLine();
//
//                                while(line2 != null){
//                                    //Log.d("testing", line);
//                                    sb2.append(line2);
//                                    line2 = reader2.readLine();
//                                }
//
//                                Log.d("testing", sb2.toString());

                                temperature = parseAndReturnCurrentTemperature(in);
                            }
                        } catch (MalformedURLException e){
                            e.printStackTrace();
                        } catch(IOException e){
                            e.printStackTrace();
                        } catch(XmlPullParserException e){
                            e.printStackTrace();
                        }


                        return new ValidateWithTemperature(true, temperature);
                    }
                    else {
                        return new ValidateWithTemperature(false, temperature);
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
        return new ValidateWithTemperature(false, "");
    }

    public String parseAndReturnCurrentTemperature(InputStream in) throws XmlPullParserException, IOException{

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line = reader.readLine();

        while(line != null){
            sb.append(line);
            line = reader.readLine();
        }

//        Log.d("testing2", sb.toString());

        XmlPullParser parser  = XmlPullParserFactory.newInstance().newPullParser();
        parser.setInput(new StringReader(sb.toString()));
        String temperature = "N/A";

        boolean breakLoop = false;
        int event = parser.getEventType();
        while(event != XmlPullParser.END_DOCUMENT && !breakLoop){
            switch (event){
                case XmlPullParser.START_TAG:
                    if(parser.getName().equals("temp_f")){
                        temperature = parser.nextText();
                        Log.d("testing", temperature);
                    }
                    break;

                case XmlPullParser.END_TAG:
                    if(parser.getName().equals("temp_f")){
                        breakLoop = true;
                    }
                    break;
            }
            event = parser.next();
        }
        return temperature;
    }

    public interface IValidateCityStateAsyncTask {
        void setValidated(ValidateWithTemperature validated);
        void showProcessing();
        void finishProcessing();
    }
}
