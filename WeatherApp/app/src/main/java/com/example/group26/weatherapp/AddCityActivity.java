package com.example.group26.weatherapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddCityActivity extends AppCompatActivity implements ValidateCityStateAsyncTask.IValidateCityStateAsyncTask{

    // fullcontact location api details/example:
    // api-key = d622bd3a3b5c8b7b
    // http://api.fullcontact.com/v2/address/locationNormalizer.json?place=Raleigh,%20NC&apiKey=d622bd3a3b5c8b7b


    // wunderground api details/example:
    //api-key = 99b6190b496ab4d7
    //http://api.wunderground.com/api/99b6190b496ab4d7/hourly/q/NC/Charlotte.xml
    EditText cityEditText;
    EditText stateEditText;

    boolean isValidated = false;
    ProgressDialog progressDialog;

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
                    String city = cityEditText.getText().toString();
                    String state = stateEditText.getText().toString();

                    String url = Constants.LOCATION_API_ENDPOINT + "place=" + city + "," + state + "&apiKey=d622bd3a3b5c8b7b";
                    new ValidateCityStateAsyncTask(AddCityActivity.this).execute(url);
                }
            }
        });
    }

    @Override
    public void setValidated(boolean validated) {
        this.isValidated = validated;

        // If we pass validation, go ahead and create a city object and pass it back to the main activity.
        if(isValidated){
            City cityObject = new City(cityEditText.getText().toString(), stateEditText.getText().toString());
            Intent resultIntent = getIntent();
            resultIntent.putExtra(Constants.CITY, cityObject);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
        else {
            Toast.makeText(AddCityActivity.this, "The city and/or state entered are not legitimate cities/states", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showProcessing() {
        progressDialog = new ProgressDialog(AddCityActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Validating input..");
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
