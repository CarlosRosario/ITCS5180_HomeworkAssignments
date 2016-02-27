package com.example.group26.imdb_app;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Carlos on 2/26/2016.
 */
public class Movie implements Serializable, Comparable<Movie> {

    String title;
    int year;
    String imdbID;
    String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String posterURL;
    String released;
    String genre;
    String director;
    String actors;
    String plot;
    String imdbRating;

    public static Movie createMovie(JSONObject json) throws JSONException{
        Movie movie = new Movie();

        movie.setTitle(json.getString("Title"));
        movie.setYear(json.getInt("Year"));
        movie.setImdbID(json.getString("imdbID"));
        movie.setType(json.getString("Type"));
        movie.setPosterURL(json.getString("Poster"));

        return movie;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    @Override
    public int compareTo(Movie compareMovie) {

        if(this.year == compareMovie.getYear()){
            return 0;
        }
        else if(this.year < compareMovie.getYear()) {
            return 1;
        }
        else if(this.year > compareMovie.getYear()) {
            return -1;
        }
        else {
            return 0;
        }
    }
}
