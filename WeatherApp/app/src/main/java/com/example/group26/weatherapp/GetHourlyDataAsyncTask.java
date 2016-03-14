package com.example.group26.weatherapp;

import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Carlos on 3/8/2016.
 */
public class GetHourlyDataAsyncTask extends AsyncTask<String, Void, List<Weather>> {

    IGetWeather activity;

    public GetHourlyDataAsyncTask(IGetWeather activity){
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.showProcessing();
    }

    @Override
    protected void onPostExecute(List<Weather> weathers) {
        super.onPostExecute(weathers);
        activity.finishProcessing();
        activity.setHourlyWeather(weathers);
    }

    @Override
    protected List<Weather> doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            int statusCode = con.getResponseCode();

            if(statusCode == HttpURLConnection.HTTP_OK){
                InputStream inputStream = con.getInputStream();
                return WeatherUtils.WeatherPullParser.parseWeather(inputStream);
            }

        }catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } catch (XmlPullParserException e){
            e.printStackTrace();
        }
        return null;
    }

    public interface IGetWeather{
        void setHourlyWeather(List<Weather> list);
        void showProcessing();
        void finishProcessing();

    }
}
