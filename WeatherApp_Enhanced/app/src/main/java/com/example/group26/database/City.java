package com.example.group26.database;

import java.io.Serializable;

/**
 * Created by Carlos on 3/5/2016.
 */
public class City implements Serializable{

    private long cityKey;
    private String cityName;
    private String state;
    private String temperature;

    public City(){

    }

    public City(String cityName, String state){
        setCityName(cityName);
        setState(state);
    }

    public City(String cityName, String state, String temperature){
        setCityName(cityName);
        setState(state);
        this.temperature = temperature;
    }

    public long getCitykey() {
        return cityKey;
    }

    public void setCitykey(long cityKey) {
        this.cityKey = cityKey;
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

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return cityName.replace("_", " ") + ", " + state.replace("_", " ");
    }
}
