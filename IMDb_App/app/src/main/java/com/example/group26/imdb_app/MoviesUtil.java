package com.example.group26.imdb_app;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Carlos on 2/26/2016.
 */
public class MoviesUtil {

    static public class MoviesJSONParser {

        static List<Movie> parseMovies(String in) throws JSONException{
            List<Movie> moviesList = new ArrayList<Movie>();
            JSONObject root = new JSONObject(in);
            JSONArray moviesJSONArray = root.getJSONArray("Search");

            for(int i = 0; i < moviesJSONArray.length(); i++){
                JSONObject movieJSONObject = moviesJSONArray.getJSONObject(i);
                Movie movie = Movie.createMovie(movieJSONObject);

                moviesList.add(movie);
            }

            // Before returning the movie list, let's sort it in descending order of year.
            Collections.sort(moviesList);

            // For debugging purposes
//            for(Movie m: moviesList){
//                Log.d("sorted?", String.valueOf(m.getYear()));
//            }

            return moviesList;
        }
    }
}
