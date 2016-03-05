package com.example.group26.weatherapp;

import java.io.Serializable;

/**
 * Created by Carlos on 3/5/2016.
 */
public class City implements Serializable{

    String cityName;
    String state;

    public City(String cityName, String state){
        // using the accessor method to set the city so that there is onl one location where we are replacing space characters with underscore characters as required by the wunderground api.
        setCityName(cityName);
        this.state = state;
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
        this.state = state;
    }
}
