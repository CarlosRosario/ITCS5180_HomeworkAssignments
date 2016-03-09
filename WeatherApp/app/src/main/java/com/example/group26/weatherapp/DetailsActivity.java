package com.example.group26.weatherapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    List<Weather> weatherList = new ArrayList<Weather>();
    Weather weatherForChosenHour;
    City city;
    int clickedPosition;

    TextView cityState;
    TextView weatherTime;
    TextView currentTemp;
    TextView currentWeather;
    TextView maxTemp;
    TextView minTemp;
    TextView feelsLike;
    TextView humidity;
    TextView dewpoint;
    TextView pressure;
    TextView clouds;
    TextView winds;
    ImageView weatherImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        cityState = (TextView) findViewById(R.id.city_state);
        weatherTime = (TextView) findViewById(R.id.weather_time);
        currentTemp = (TextView) findViewById(R.id.current_temp);
        currentWeather = (TextView) findViewById(R.id.current_weather);
        maxTemp = (TextView) findViewById(R.id.max_temp);
        minTemp = (TextView) findViewById(R.id.min_temp);
        feelsLike = (TextView) findViewById(R.id.feels_like);
        humidity = (TextView) findViewById(R.id.humidity);
        dewpoint = (TextView) findViewById(R.id.dewpoint);
        pressure = (TextView) findViewById(R.id.pressure);
        clouds = (TextView) findViewById(R.id.clouds);
        winds = (TextView) findViewById(R.id.winds);
        weatherImage = (ImageView) findViewById(R.id.weather_image);

        if(getIntent() != null && getIntent().getExtras() != null) {
            clickedPosition = getIntent().getExtras().getInt(Constants.POSITIONCLICKED);

            if(getIntent().getExtras().getSerializable(Constants.CITY) != null){
                city = (City)getIntent().getExtras().getSerializable(Constants.CITY);
            }

            if(getIntent().getExtras().getSerializable(Constants.HOURCLICKED) != null){
                weatherForChosenHour = (Weather)getIntent().getExtras().getSerializable(Constants.HOURCLICKED);
            }

            if(getIntent().getExtras().getSerializable(Constants.DETAILS) != null) {
                Weather[] weatherArray = (Weather[])getIntent().getExtras().getSerializable(Constants.DETAILS);
                weatherList = Arrays.asList(weatherArray);
            }
        }


        // Populate Data
        if(weatherForChosenHour != null){
            hydrate(weatherForChosenHour);
        }

        findViewById(R.id.arrow_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedPosition--;
                if (clickedPosition < 0) {
                    clickedPosition = weatherList.size() - 1;
                }

                weatherForChosenHour = weatherList.get(clickedPosition);
                hydrate(weatherForChosenHour);
            }
        });

        findViewById(R.id.arrow_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedPosition++;
                if (clickedPosition > weatherList.size() - 1) {
                    clickedPosition = 0;
                }
                weatherForChosenHour = weatherList.get(clickedPosition);
                hydrate(weatherForChosenHour);
            }
        });
    }

    public void hydrate(Weather weather){
        cityState.setText(city.getCityName().replace("_", " ") + ", " + city.getState().replace("_", " "));
        weatherTime.setText(weather.getTime() + " " + weather.getDay());

        if(!weather.getIconURL().isEmpty() && !weather.getIconURL().equals("null thumbUrl")){
            Picasso.with(DetailsActivity.this).load(weather.getIconURL()).into(weatherImage);
        }

        currentTemp.setText(weather.getTemperature() + Constants.DEGREES_UNICODE + "F");
        currentWeather.setText(weather.getClimateType());
        maxTemp.setText("Max Temperature: " + weather.getMaximumTemp() + Constants.DEGREES_UNICODE + "F");
        minTemp.setText("Min Temperature: " + weather.getMinimumTemp() + Constants.DEGREES_UNICODE + "F");
        feelsLike.setText(weather.getFeelsLike() + Constants.DEGREES_UNICODE + "F");
        humidity.setText(weather.getHumidity() + "%");
        dewpoint.setText(weather.getDewpoint() + " Fahrenheit");
        pressure.setText(weather.getPressure() + " hPa");
        clouds.setText(weather.getClouds());
        winds.setText(weather.getWindSpeed() + " mph, " + weather.getWindDegrees() + Constants.DEGREES_UNICODE + " " + transformWindDirection(weather.getWindDirection()));
    }

    public String transformWindDirection(String windDirectionAbbreviation){
        windDirectionAbbreviation = windDirectionAbbreviation.toUpperCase();

        if(windDirectionAbbreviation.equals("W")){
            return "West";
        }
        else if(windDirectionAbbreviation.equals("N")){
            return "North";
        }
        else if(windDirectionAbbreviation.equals("E")){
            return "East";
        }
        else if(windDirectionAbbreviation.equals("S")){
            return "South";
        }
        else if(windDirectionAbbreviation.equals("SSW")){
            return "South-SouthWest";
        }
        else if(windDirectionAbbreviation.equals("NNW")){
            return "North-NorthWest";
        }
        else if(windDirectionAbbreviation.equals("NNE")){
            return "North-NorthEast";
        }
        else if(windDirectionAbbreviation.equals("ESE")){
            return "East-SouthEast";
        }
        else if(windDirectionAbbreviation.equals("ENE")){
            return "East-NorthEast";
        }
        else if(windDirectionAbbreviation.equals("SSE")){
            return "South-SouthEast";
        }
        else if(windDirectionAbbreviation.equals("WSW")){
            return "West-SouthWest";
        }
        else if(windDirectionAbbreviation.equals("WNW")){
            return "West-NorthWest";
        }
        else if(windDirectionAbbreviation.equals("SW")){
            return "SouthWest";
        }
        else if(windDirectionAbbreviation.equals("SE")){
            return "SouthEast";
        }
        else if(windDirectionAbbreviation.equals("NW")){
            return "NorthWest";
        }
        else if(windDirectionAbbreviation.equals("NE")){
            return "NorthEast";
        }
        else {
            return windDirectionAbbreviation;
        }
    }
}
