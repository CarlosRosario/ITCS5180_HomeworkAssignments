package com.example.group26.stayintouchwithfragments;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Carlos on 4/27/2016.
 */
public class MessageAdapter extends ArrayAdapter<Message> {
    List<Message> mData;
    Context mContext;
    int mResource;
    String myName;
    ViewMessagesFragment viewMessagesFragment;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }

        final Message message = mData.get(position);
        if(message.getDeletedBy() == null || message.getDeletedBy().isEmpty()){
            if(message.getSender().equals(myName)){
                // This is one of "my" messages - lets color it beige
                convertView.setBackgroundColor(Color.LTGRAY);
            }
            else {
                convertView.setBackgroundColor(Color.WHITE);
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
                    final Firebase firebase = new Firebase("https://stayintouchfrag-5180.firebaseio.com/");
                    final String messageTimeStamp = message.getTimeStamp(); // timestamp is most unique out of all fields inside of message object

                    Query queryRef = firebase.child("Messages").orderByChild("timeStamp").equalTo(messageTimeStamp);
                    queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            HashMap data = (HashMap) dataSnapshot.getValue();

                            // this loop only runs one time - even without the break statement
                            for (Object key : data.keySet()) {
                                HashMap innerData = (HashMap) data.get(key);

                                String uniqueKeyForUser = key.toString();
                                firebase.child("Messages").child(uniqueKeyForUser).removeValue();
                                viewMessagesFragment.removeMessage(message);
                                break;
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                        }
                    });
            }
        });
        }
        return convertView;
    }

    public MessageAdapter(Context context, int resource, List<Message> objects, String myName, ViewMessagesFragment viewMessagesFragment) {
        super(context, resource, objects);
        this.mData = objects;
        this.mContext = context;
        this.mResource = resource;
        this.myName = myName;
        this.viewMessagesFragment = viewMessagesFragment;
    }
}