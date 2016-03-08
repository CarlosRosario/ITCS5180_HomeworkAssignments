package com.example.group26.weatherapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HourlyDataActivity extends AppCompatActivity implements GetHourlyDataAsyncTask.IGetWeather {

    List<Weather> hourlyData = new ArrayList<Weather>();
    ListView listView;
    City city;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_data);

        if(getIntent() != null && getIntent().getExtras() != null){
            city = (City)getIntent().getExtras().getSerializable(Constants.CITY);

            if(city  != null){
                String url = Constants.WUNDERGROUND_API_ENDPOINT + city.getState() + "/" + city.getCityName() + ".xml";
                //Log.d("url", url);
                new GetHourlyDataAsyncTask(HourlyDataActivity.this).execute(url);
            }
        }
    }

    @Override
    public void setHourlyWeather(List<Weather> list) {
        this.hourlyData = list;

        TextView hourlyDataTextView = (TextView)findViewById(R.id.hourly_header_textView);
        TextView hourlyDataLocationTextView = (TextView)findViewById(R.id.hourly_header_location_textView);

        hourlyDataTextView.setText("Current Location: ");
        hourlyDataLocationTextView.setText(city.getCityName().replace("_", " ") + ", " + city.getState().replace("_", " "));

        // Weather Adapter stuff here.
        WeatherAdapter weatherAdapter = new WeatherAdapter(HourlyDataActivity.this,R.layout.row_item_layout, hourlyData);
        listView = (ListView)findViewById(R.id.topStoryListView);
        listView.setAdapter(weatherAdapter);
        weatherAdapter.setNotifyOnChange(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Create Details Activity
                Intent detailsActivityIntent = new Intent(HourlyDataActivity.this, DetailsActivity.class);
                detailsActivityIntent.putExtra(Constants.POSITIONCLICKED, position);
                detailsActivityIntent.putExtra(Constants.HOURCLICKED, hourlyData.get(position));
                detailsActivityIntent.putExtra(Constants.CITY, city);

                // Convert question list into a question array so that we can pass it via putExtra()
                Weather[] weatherArray = new Weather[hourlyData.size()];
                for(int i = 0; i < hourlyData.size(); i++){
                    weatherArray[i] = hourlyData.get(i);
                }

                detailsActivityIntent.putExtra(Constants.DETAILS, weatherArray);
                startActivity(detailsActivityIntent);
            }
        });

    }

    @Override
    public void showProcessing() {
        progressDialog = new ProgressDialog(HourlyDataActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Hourly Data");
        progressDialog.show();
    }

    @Override
    public void finishProcessing() {
        progressDialog.dismiss();
    }
}
