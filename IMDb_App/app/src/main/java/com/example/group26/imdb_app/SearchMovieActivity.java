package com.example.group26.imdb_app;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchMovieActivity extends AppCompatActivity implements GetMovieDataAsyncTask.IGetMovieData {

    List<Movie> moviesList = new ArrayList<Movie>();
    Map<String, Integer> movieMap = new HashMap<String, Integer>();
    String searchedMovie;
    String url;
    LinearLayout linearLayout;
    ProgressDialog progressDialog;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);

        linearLayout = (LinearLayout)findViewById(R.id.containerLinearLayout);

        if(getIntent() != null && getIntent().getExtras() != null ){
            searchedMovie = getIntent().getExtras().getString(Constants.SEARCH_KEY);

            // Build imdb api url string
            url = Constants.OMDB_ENDPOINT + "?type=movie&s=" + searchedMovie;
        }

        if(isConnectedOnline()){
            new GetMovieDataAsyncTask(this).execute(url);
        }else {
            Toast.makeText(SearchMovieActivity.this, "No network connection", Toast.LENGTH_LONG).show();
        }
    }

    // Method to check if device is connected to network
    private boolean isConnectedOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // We can use networkInfo.getType() to figure out what kind of network the device is connected to (wifi, ethernet, bluetooth, etc)
        if(networkInfo != null && networkInfo.isConnected()){
            return  true;
        }
        return false;
    }

    @Override
    public void setMovies(final List<Movie> moviesList) {
        this.moviesList = moviesList;

        if(this.moviesList != null && !this.moviesList.isEmpty() && this.moviesList.size() > 0){
            for(int i = 0; i < moviesList.size(); i++){
                Movie m = moviesList.get(i);
                movieMap.put(m.getImdbID(), i);

                Log.d("final", "Movie Title: " + m.getTitle());
                Log.d("final", "Movie Year: " + m.getYear());
                Log.d("final", "Movie imdbID: " + m.getImdbID());
                Log.d("final", "Movie Type: " + m.getType());
                Log.d("final", "Movie Poster: " + m.getPosterURL());

                textView = new TextView(SearchMovieActivity.this);
                textView.setText(m.getTitle() + " (" + m.getYear() + ")");
                textView.setTag(m.getImdbID());
                textView.setGravity(Gravity.LEFT);
                textView.setClickable(true);
                textView.setPadding(30, 30, 0, 30);
                textView.setTextColor(Color.BLACK);
                textView.setTextSize(20);


                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent movieDetailsIntent = new Intent(SearchMovieActivity.this, MovieDetailsActivity.class);

                        // Convert movie list into a movie array so that we can pass it via putExtra()
                        Movie[] movieArray = new Movie[moviesList.size()];
                        for(int i = 0; i < moviesList.size(); i++){
                            movieArray[i] = moviesList.get(i);
                        }
                        movieDetailsIntent.putExtra(Constants.MOVIES, movieArray);

                        // Pass the index of the movie that was clicked
                        if(v != null && v.getTag() != null && !v.getTag().toString().isEmpty()){
                            String key = v.getTag().toString();
                            int selectedMovieIndex = movieMap.get(key);
                            movieDetailsIntent.putExtra(Constants.SELECTED_MOVIE, selectedMovieIndex);
                        }
                        startActivity(movieDetailsIntent);
                    }
                });

                linearLayout.addView(textView);

                // Add separator line - might not be the correct way to do it.. investigate further once functionality is complete.
                TextView separator = new TextView(SearchMovieActivity.this);
                separator.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
                separator.setBackgroundColor(Color.DKGRAY);
                linearLayout.addView(separator);
            }
        }
    }

    @Override
    public void showProcessing() {
        progressDialog = new ProgressDialog(SearchMovieActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading Movie List");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    @Override
    public void finishProcessing() {
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }
}
