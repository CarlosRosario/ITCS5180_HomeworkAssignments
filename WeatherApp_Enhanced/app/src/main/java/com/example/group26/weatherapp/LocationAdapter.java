package com.example.group26.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.group26.database.City;

import java.util.List;

/**
 * Created by Carlos on 3/18/2016.
 */
public class LocationAdapter extends ArrayAdapter<City> {

    List<City> mData;
    Context mContext;
    int mResource;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }

        City city = mData.get(position);

        //  Set time
        TextView locationTextView = (TextView)convertView.findViewById(R.id.locationTextView);
        locationTextView.setText(city.getCityName().replace("_", " ") + ", " + city.getState().replace("_", " "));

        // Set climate
        TextView locationTemperatureTextView = (TextView)convertView.findViewById(R.id.locationTemperatureTextView);
        locationTemperatureTextView.setText(city.getTemperature() + Constants.DEGREES_UNICODE + "F");

        return convertView;

    }

    public LocationAdapter(Context context, int resource, List<City> objects){
        super(context,resource,objects);
        this.mData = objects;
        this.mContext = context;
        this.mResource = resource;
    }
}
