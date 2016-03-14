package com.example.group26.weatherapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Carlos on 3/8/2016.
 */
public class WeatherAdapter extends ArrayAdapter<Weather> {

    List<Weather> mData;
    Context mContext;
    int mResource;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }

        Weather weather = mData.get(position);

        //  Set time
        TextView timeTextView = (TextView)convertView.findViewById(R.id.timeTextView);
        timeTextView.setText(weather.getTime() + " " + weather.getDay());

        // Set climate
        TextView climateTypeTextView = (TextView)convertView.findViewById(R.id.climateTypeTextView);
        climateTypeTextView.setText(weather.getClimateType());

        // Set temperature
        TextView temperatureTextView = (TextView)convertView.findViewById(R.id.temperatureTextView);
        temperatureTextView.setText(weather.getTemperature() + Constants.DEGREES_UNICODE + "F");

        // Set icon
        ImageView thumbnail = (ImageView)convertView.findViewById(R.id.weatherIconImageView);
        Log.d("thumbnail", weather.getIconURL());

        if(!weather.getIconURL().isEmpty() && !weather.getIconURL().equals("null thumbUrl")){
            Picasso.with(mContext).load(weather.getIconURL()).into(thumbnail);
        }

        return convertView;
    }

    public WeatherAdapter(Context context, int resource, List<Weather> objects){
        super(context,resource,objects);
        this.mData = objects;
        this.mContext = context;
        this.mResource = resource;
    }
}
