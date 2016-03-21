package com.example.group26.weatherapp;

/**
 * Created by Carlos on 3/18/2016.
 */
public class ValidateWithTemperature {

    private boolean isValidated;
    private String temperature;

    public ValidateWithTemperature(boolean isValidated, String temperature) {
        this.isValidated = isValidated;
        this.temperature = temperature;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public void setIsValidated(boolean isValidated) {
        this.isValidated = isValidated;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
