package com.example.group26.imdb_app;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Carlos on 2/26/2016.
 */
public class GetMovieDataAsyncTask extends AsyncTask<String, Void, List<Movie>> {

    IGetMovieData activity;

    public GetMovieDataAsyncTask(IGetMovieData activity){
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.showProcessing();
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        super.onPostExecute(movies);
        activity.setMovies(movies);
        activity.finishProcessing();
    }

    @Override
    protected List<Movie> doInBackground(String... params) {

        try {

            // Make sure we handle spaces in the url correctly.
            String formatedURL = params[0];
            formatedURL = formatedURL.replace(" ", "%20");
            Log.d("url", formatedURL);

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
                    Log.d("testing", line );
                    sb.append(line);
                    line = reader.readLine();
                }

                try {
                    Log.d("testing", sb.toString());
                    // Testing that the process dialog indeed works
//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    return MoviesUtil.MoviesJSONParser.parseMovies(sb.toString());
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public interface IGetMovieData{
        void setMovies(List<Movie> moviesList);
        void showProcessing();
        void finishProcessing();
    }
}
