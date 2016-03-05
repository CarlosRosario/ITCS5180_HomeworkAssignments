package com.example.group26.weatherapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddCityActivity extends AppCompatActivity {

    //api-key = 99b6190b496ab4d7
    //http://api.wunderground.com/api/99b6190b496ab4d7/hourly/q/NC/Charlotte.xml
    EditText cityEditText;
    EditText stateEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);

        cityEditText = (EditText)findViewById(R.id.cityEditText);
        stateEditText = (EditText)findViewById(R.id.stateEditText);

        findViewById(R.id.saveCityButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cityEditText != null && cityEditText.getText() != null && stateEditText != null && stateEditText.getText() != null){

                    // Somehow validate that the city and state are legitimate city/state in the united states. The only way i can think to do this is to
                    // leverage the wunderground API somehow. Certainly we cannot keep a dictionary of all cities in the united states for a homework assignment.


                    // If we pass validation, go ahead and create a city object and pass it back to the main activity.
                    City city = new City(cityEditText.getText().toString(), stateEditText.getText().toString());
                    Intent resultIntent = getIntent();
                    resultIntent.putExtra(Constants.CITY, city);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }

            }
        });

    }
}
