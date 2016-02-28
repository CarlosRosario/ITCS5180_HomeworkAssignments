package com.example.group26.imdb_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity implements GetInDepthMovieDetailAsyncTask.IGetInDepthMovieDetails {

    Movie currentlySelectedMovie;
    List<Movie> moviesList = new ArrayList<Movie>();
    int selectedMovieIndex = -1;
    ProgressDialog progressDialog;

    TextView title;
    TextView releaseDate;
    TextView genre;
    TextView director;
    TextView actors;
    TextView plot;
    ImageView poster;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        title = (TextView)findViewById(R.id.movieTitleTextView);
        releaseDate = (TextView)findViewById(R.id.releaseDateTextView);
        genre = (TextView)findViewById(R.id.genreTextView);
        director = (TextView)findViewById(R.id.directorTextView);
        actors = (TextView)findViewById(R.id.actorsTextView);
        plot = (TextView)findViewById(R.id.plotTextView);
        poster = (ImageView)findViewById(R.id.posterImageView);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);

        if(getIntent() != null && getIntent().getExtras() != null ){
            Movie[] movieArray = (Movie[])getIntent().getExtras().getSerializable(Constants.MOVIES);
            moviesList = Arrays.asList(movieArray);

            // For debugging purposes
//            for(Movie m: moviesList){
//                Log.d("details", "Movie Title: " + m.getTitle());
//                Log.d("details", "Movie Year: " + m.getYear());
//                Log.d("details", "Movie imdbID: " + m.getImdbID());
//                Log.d("details", "Movie Type: " + m.getType());
//                Log.d("details", "Movie Poster: " + m.getPosterURL());
//            }

            selectedMovieIndex = getIntent().getExtras().getInt(Constants.SELECTED_MOVIE);
            currentlySelectedMovie = moviesList.get(selectedMovieIndex);
            new GetInDepthMovieDetailAsyncTask(this).execute(currentlySelectedMovie);
            //Log.d("details", "Selected Movie Index: " + String.valueOf(selectedMovieIndex));
        }


        // Poster click
        poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent movieWebViewIntent = new Intent(MovieDetailsActivity.this, MovieWebView.class);
                movieWebViewIntent.putExtra(Constants.IMDB_ID, currentlySelectedMovie.getImdbID());
                startActivity(movieWebViewIntent);
            }
        });

        // Left click
        findViewById(R.id.leftArrowImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedMovieIndex--;
                if(selectedMovieIndex < 0){
                    selectedMovieIndex = moviesList.size() - 1;
                }
                currentlySelectedMovie = moviesList.get(selectedMovieIndex);
                new GetInDepthMovieDetailAsyncTask(MovieDetailsActivity.this).execute(currentlySelectedMovie);
            }
        });

        // Right click
        findViewById(R.id.rightArrowImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedMovieIndex++;
                if(selectedMovieIndex > moviesList.size() -1){
                    selectedMovieIndex = 0;
                }
                currentlySelectedMovie = moviesList.get(selectedMovieIndex);
                new GetInDepthMovieDetailAsyncTask(MovieDetailsActivity.this).execute(currentlySelectedMovie);
            }
        });

        // Finish click
        findViewById(R.id.finishButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void hydrate(Bitmap b) {

        Movie movie = moviesList.get(selectedMovieIndex);

        title.setText(movie.getTitle());

        try {
            String releaseDateString = movie.getReleased();
            DateFormat inputFormat = new SimpleDateFormat("dd MMM yyyy");
            DateFormat outputFormat = new SimpleDateFormat("MMM dd yyyy");
            Date date = inputFormat.parse(releaseDateString);
            releaseDateString = outputFormat.format(date);
            releaseDate.setText(releaseDateString);
        } catch (ParseException e) {
            releaseDate.setText(movie.getReleased());
            e.printStackTrace();
        }

        genre.setText(movie.getGenre());
        director.setText(movie.getDirector());
        actors.setText(movie.getActors());
        plot.setText(movie.getPlot());
        poster.setImageBitmap(b);


        try{
            if(!movie.getImdbRating().isEmpty() && !movie.getImdbRating().equals("N/A")){
                float actualRating = Float.parseFloat(movie.getImdbRating());
                ratingBar.setRating(actualRating/2);
            }
        } catch (NumberFormatException e){
            e.printStackTrace();
        }

    }

    @Override
    public void showProcessing() {
        progressDialog = new ProgressDialog(MovieDetailsActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading Movie");
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
