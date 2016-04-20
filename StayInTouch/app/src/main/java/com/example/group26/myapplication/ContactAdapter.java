package com.example.group26.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Carlos on 4/18/2016.
 */
public class ContactAdapter extends ArrayAdapter<User> {

    List<User> mData;
    //Map<String, List<Message>> auxilaryData;
    Context mContext;
    int mResource;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }


        final User contact = mData.get(position);

        // Set contact profile picture
        ImageView profilePictureImageView = (ImageView)convertView.findViewById(R.id.conversationsActivityProfileImageView);
        String base64ProfilePicture = contact.getBase64Picture();
        if(base64ProfilePicture != null && !base64ProfilePicture.isEmpty()){
            byte[] decodedImageByteArray = Base64.decode(base64ProfilePicture, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(decodedImageByteArray, 0, decodedImageByteArray.length);
            profilePictureImageView.setImageBitmap(decodedImage);
            Log.d("test", "setting profile pic");
        }

        // Set contact first and last name
        TextView fullNameTextview = (TextView)convertView.findViewById(R.id.conversationsActivityFullNameTextView);
        String fullName = contact.getFullName();
        if(fullName != null && !fullName.isEmpty()){
            fullNameTextview.setText(fullName);
            Log.d("test", "setting name");
        }

        // Set red bubble if applicable
        final ImageView redBubbleImageView = (ImageView)convertView.findViewById(R.id.conversationsActivityRedBubbleImageView);
        final List<Message> messagesForThisContact = new ArrayList<Message>();
        final Firebase firebase = new Firebase("https://stayintouch-5180.firebaseio.com/");
        Query queryRef = firebase.child("Messages").orderByChild("email").equalTo(contact.getEmail());
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Message contactMessage = messageSnapshot.getValue(Message.class);
                    messagesForThisContact.add(contactMessage);
                }

                for(Message message: messagesForThisContact){
                    if(!message.isMessage_read()){
                        Picasso.with(mContext).load(R.drawable.redbubble).into(redBubbleImageView);
                        break;
                    }
                }
            }
                @Override
                public void onCancelled (FirebaseError firebaseError){


                }
        });

        // Set phone icon
        Log.d("test", "setting phone icon");
        ImageView phoneIconImageView = (ImageView)convertView.findViewById(R.id.conversationsActivityPhoneIconImageView);
        Picasso.with(mContext).load(R.drawable.phoneicon).into(phoneIconImageView);

        phoneIconImageView.setOnClickListener(new View.OnClickListener() {

            final int REQUEST_CODE_ASK_PERMISSIONS = 100;
            @Override
            public void onClick(View v) {
                String selectedPhoneNumber = contact.getPhoneNumber();
                Log.d("test", "clicked phone is: " + selectedPhoneNumber);
                ((ConversationsActivity)mContext).setPermissionsPhoneNumber(selectedPhoneNumber);


                int hasPhonePermissions = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE);
                if(hasPhonePermissions != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions((Activity)mContext, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_ASK_PERMISSIONS);
                    return;
                }

                Intent phoneCallIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + selectedPhoneNumber));
                try {
                    mContext.startActivity(phoneCallIntent);
                }
                catch(SecurityException e){
                    Log.d("error", e.getMessage());
                    Log.d("error", e.getStackTrace().toString());
                }
            }
        });
        return convertView;
    }

    public ContactAdapter(Context context, int resource, List<User> objects/*, Map<String, List<Message>> auxilaryData*/){
        super(context, resource, objects);
        this.mData = objects;
        //this.auxilaryData = auxilaryData;
        this.mContext = context;
        this.mResource = resource;
    }
}
