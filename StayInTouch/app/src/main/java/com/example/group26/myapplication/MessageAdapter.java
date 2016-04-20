package com.example.group26.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.os.*;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by meredithbrowne on 4/19/16.
 */
public class MessageAdapter extends ArrayAdapter<Message> {
    List<Message> mData;
    Context mContext;
    int mResource;
    String myName;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }

        Message message = mData.get(position);

        if(message.getSender().equals(myName)){
            // This is one of "my" messages - lets color it beige
            convertView.setBackgroundColor(Color.LTGRAY);
        }

        TextView viewMessagesNameTextView = (TextView) convertView.findViewById(R.id.viewMessagesNameTextView);
        String sender = message.getSender();
        if (sender != null && !sender.isEmpty()) {
            viewMessagesNameTextView.setText(sender);
        }
        TextView viewMessagesMessageTextView = (TextView) convertView.findViewById(R.id.viewMessagesMessageTextView);
        String messageContent = message.getMessage_text();
        if (messageContent != null && !messageContent.isEmpty()) {
            viewMessagesMessageTextView.setText(messageContent);
        }
        TextView viewMessagesTimestampTextView = (TextView) convertView.findViewById(R.id.viewMessagesTimestampTextView);
        String timestamp = message.getTimeStamp();
        if (timestamp != null && !timestamp.isEmpty()) {

            SimpleDateFormat inputFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            SimpleDateFormat outputFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            try {
                Date date = inputFormatter.parse(timestamp);
                viewMessagesTimestampTextView.setText(outputFormatter.format(date));
            }
            catch (ParseException e){
                Log.d("err", e.getMessage());
                Log.d("err", e.getStackTrace().toString());
            }
        }

        ImageView trashCan = (ImageView)convertView.findViewById(R.id.viewMessagesActivityTrashIconImageView);
        trashCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "clicked trash can", Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    public MessageAdapter(Context context, int resource, List<Message> objects, String myName) {
        super(context, resource, objects);
        this.mData = objects;
        this.mContext = context;
        this.mResource = resource;
        this.myName = myName;
    }
}
