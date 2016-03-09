package com.example.group26.weatherapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter<City> adapter;
    List<City> cities = new ArrayList<City>();
    TextView defaultTextView;
    ListView listView;

    boolean isDefaultTextViewShowing = true;
    boolean isListViewShowing = false;

    @Override
       protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        defaultTextView = (TextView)findViewById(R.id.noCitiesMessage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_customactionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.action_add_city :
                Intent addCityActivityIntent = new Intent(MainActivity.this, AddCityActivity.class);
                startActivityForResult(addCityActivityIntent, Constants.ADDCITY_ACTIVITY);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case Constants.ADDCITY_ACTIVITY :
                if(resultCode == Activity.RESULT_OK && data != null){

                    // Add city to city list
                    City city = (City) data.getSerializableExtra(Constants.CITY);
                    if(city != null){
                        if(adapter == null && isDefaultTextViewShowing){
                            cities.add(city);
                        }
                    }

                    // If city list has one or more cities, the Default TextView should be removed and a ListView should be created displaying the city names
                    final RelativeLayout layout = (RelativeLayout) findViewById(R.id.mainLayout);
                    if(cities.size() >= 1){
                        if(isDefaultTextViewShowing){
                            // Remove the default textview
                            layout.removeView(defaultTextView);

                            // Add a ListView
                            listView = new ListView(MainActivity.this);
                            listView.setBackgroundColor(Color.LTGRAY);
                            adapter = new ArrayAdapter<City>(MainActivity.this, android.R.layout.simple_list_item_1, cities);
                            listView.setAdapter(adapter);
                            adapter.setNotifyOnChange(true);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent hourlyDataActivityIntent = new Intent(MainActivity.this, HourlyDataActivity.class);
                                    hourlyDataActivityIntent.putExtra(Constants.CITY, cities.get(position));
                                    startActivity(hourlyDataActivityIntent);
                                }
                            });

                            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                                    if(adapter != null){
                                        adapter.remove(cities.get(position));

                                        if(cities.size() < 1){
                                            if(isListViewShowing){
                                                // Remove ListView
                                                layout.removeView(listView);
                                                adapter = null;

                                                // Re-add default textview
                                                layout.addView(defaultTextView);
                                                isDefaultTextViewShowing = true;
                                                isListViewShowing = false;
                                            }
                                        }
                                    }

                                    return false;
                                }
                            });

                            layout.addView(listView);
                            isDefaultTextViewShowing = false;
                            isListViewShowing = true;
                        }
                        else if(isListViewShowing){
                            if(adapter != null){
                                adapter.add(city);
                            }
                        }
                    }
                    else {
                        if(isListViewShowing){
                            // Remove ListView
                            layout.removeView(listView);

                            // Re-add default textview
                            layout.addView(defaultTextView);
                            isDefaultTextViewShowing = true;
                            isListViewShowing = false;
                        }
                    }
                }
                break;
        }
    }
}
