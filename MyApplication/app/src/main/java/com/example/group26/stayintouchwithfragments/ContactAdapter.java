package com.example.group26.stayintouchwithfragments;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlos on 4/18/2016.
 */
public class ContactAdapter extends ArrayAdapter<User> {

    List<User> mData;
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
        ImageView profilePictureImageView = (ImageView)convertView.findViewById(R.id.contactsActivityProfileImageView);
        String base64ProfilePicture = contact.getBase64Picture();
        if(base64ProfilePicture != null && !base64ProfilePicture.isEmpty()){
            byte[] decodedImageByteArray = Base64.decode(base64ProfilePicture, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(decodedImageByteArray, 0, decodedImageByteArray.length);
            profilePictureImageView.setImageBitmap(decodedImage);
        }

        // Set contact first and last name
        TextView fullNameTextview = (TextView)convertView.findViewById(R.id.contactsActivityFullNameTextView);
        String fullName = contact.getFullName();
        if(fullName != null && !fullName.isEmpty()){
            fullNameTextview.setText(fullName);
        }

        ImageView phoneIconImageView = (ImageView)convertView.findViewById(R.id.contactsActivityPhoneIconImageView);
        phoneIconImageView.setOnClickListener(new View.OnClickListener() {

            final int REQUEST_CODE_ASK_PERMISSIONS = 100;
            @Override
            public void onClick(View v) {
                String selectedPhoneNumber = contact.getPhoneNumber();
                ((MainActivity)mContext).setPermissionsPhoneNumber(selectedPhoneNumber);

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

    public ContactAdapter(Context context, int resource, List<User> objects){
        super(context, resource, objects);
        this.mData = objects;
        this.mContext = context;
        this.mResource = resource;
    }
}