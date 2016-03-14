package com.example.group26.weatherapp;

import java.io.Serializable;

/**
 * Created by Carlos on 3/5/2016.
 */
public class City implements Serializable{

    String cityName;
    String state;

    public City(String cityName, String state){
        setCityName(cityName);
        setState(state);
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName.replace(" ", "_");
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state.replace(" ", "_"); // Just in case user enters something like "North Carolina"
    }

    @Override
    public String toString() {
        return cityName.replace("_", " ") + ", " + state.replace("_", " ");
    }
}
