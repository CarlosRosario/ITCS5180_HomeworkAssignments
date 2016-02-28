package com.example.group26.imdb_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
 * Created by Carlos on 2/28/2016.
 */
public class GetInDepthMovieDetailAsyncTask extends AsyncTask<Movie, Void, Bitmap> {

    IGetInDepthMovieDetails activity;

    public GetInDepthMovieDetailAsyncTask(IGetInDepthMovieDetails activity){
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        activity.showProcessing();

    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        activity.hydrate(bitmap);
        activity.finishProcessing();
    }

    @Override
    protected Bitmap doInBackground(Movie... params) {

        Movie movie = null;
        try {

            // Update movie data
            movie = params[0];
            String formattedURL = Constants.OMDB_ENDPOINT + "?i=" + movie.getImdbID();
            URL url = new URL(formattedURL);
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
                    JSONObject root = new JSONObject(sb.toString());
                    movie.setReleased(root.getString("Released"));
                    movie.setGenre(root.getString("Genre"));
                    movie.setDirector(root.getString("Director"));
                    movie.setActors(root.getString("Actors"));
                    movie.setPlot(root.getString("Plot"));
                    movie.setImdbRating(root.getString("imdbRating"));

                } catch (JSONException e){
                    e.printStackTrace();
                }
            }

            // Get Poster Image
            if(movie != null && !movie.getPosterURL().isEmpty()){
                connection.disconnect();
                url = new URL(movie.getPosterURL());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                return bitmap;
            }

        }catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public interface IGetInDepthMovieDetails{
        void hydrate(Bitmap b);
        void showProcessing();
        void finishProcessing();
    }
}
