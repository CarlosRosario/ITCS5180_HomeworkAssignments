package com.example.group26.imdb_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity {

    List<Movie> moviesList = new ArrayList<Movie>();
    int selectedMovieIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        if(getIntent() != null && getIntent().getExtras() != null ){
            Movie[] movieArray = (Movie[])getIntent().getExtras().getSerializable(Constants.MOVIES);
            moviesList = Arrays.asList(movieArray);

            // For debugging purposes
            for(Movie m: moviesList){
                Log.d("details", "Movie Title: " + m.getTitle());
                Log.d("details", "Movie Year: " + m.getYear());
                Log.d("details", "Movie imdbID: " + m.getImdbID());
                Log.d("details", "Movie Type: " + m.getType());
                Log.d("details", "Movie Poster: " + m.getPosterURL());
            }

            selectedMovieIndex = getIntent().getExtras().getInt(Constants.SELECTED_MOVIE);
            Log.d("details", "Selected Movie Index: " + String.valueOf(selectedMovieIndex));
        }
    }
}
