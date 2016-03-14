package com.preetiharkanth.listview.inclass07.group26.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Ajay Wisawe on 2/29/2016.
 */
public class StoryAdaptor extends ArrayAdapter<Story> {

    List<Story> mData;
    Context mContext;
    int mResource;

    /**
     * {@inheritDoc}
     *
     * @param position
     * @param convertView
     * @param parent
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource,parent,false);
        }

        Story story = mData.get(position);
        TextView titleTextView = (TextView)convertView.findViewById(R.id.titleTextView);
        titleTextView.setText(story.getTitle());


        // Convert date before setting it

        String formattedDateString = "";
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd");
            Date date = inputFormat.parse(story.getCreated_date());
            formattedDateString = outputFormat.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView dateTextView = (TextView)convertView.findViewById(R.id.dateTextView);
        if(!formattedDateString.isEmpty()){
            dateTextView.setText(formattedDateString);
        }
        else {
            dateTextView.setText(story.getCreated_date());
        }

        ImageView thumbnail = (ImageView)convertView.findViewById(R.id.imageView);
        //thumbnail.setImageBitmap();

        Log.d("thumbnail", story.getThumbUrl());

        if(!story.getThumbUrl().equals("null thumbUrl")){
            Picasso.with(mContext).load(story.getThumbUrl()).into(thumbnail);
        }
        return convertView;
    }

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public StoryAdaptor(Context context, int resource, List<Story> objects) {
        super(context, resource, objects);
        this.mData = objects;
        this.mContext = context;
        this.mResource = resource;
    }
}
