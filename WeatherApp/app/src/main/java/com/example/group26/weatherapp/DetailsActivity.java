package com.example.group26.weatherapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TextView cityState = (TextView) findViewById(R.id.city_state);
        TextView weatherTime = (TextView) findViewById(R.id.weather_time);
        ImageView weatherImage = (ImageView) findViewById(R.id.weather_image);
        TextView currentTemp = (TextView) findViewById(R.id.current_temp);
        TextView currentWeather = (TextView) findViewById(R.id.current_weather);
        TextView maxTemp = (TextView) findViewById(R.id.max_temp);
        TextView minTemp = (TextView) findViewById(R.id.min_temp);
        TextView feelsLike = (TextView) findViewById(R.id.feels_like);
        TextView humidity = (TextView) findViewById(R.id.humidity);
        TextView dewpoint = (TextView) findViewById(R.id.dewpoint);
        TextView pressure = (TextView) findViewById(R.id.pressure);
        TextView clouds = (TextView) findViewById(R.id.clouds);
        TextView winds = (TextView) findViewById(R.id.winds);

        cityState.setText("");
        weatherTime.setText("");
        //weatherImage.setImageResource();
        //weatherImage.setImageBitmap();
        currentTemp.setText("");
        currentWeather.setText("");
        maxTemp.setText("Max Temperature: ");
        minTemp.setText("Min Temperature: ");
        feelsLike.setText("");
        humidity.setText("");
        dewpoint.setText("");
        pressure.setText("");
        clouds.setText("");
        winds.setText("");

        findViewById(R.id.arrow_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.arrow_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
