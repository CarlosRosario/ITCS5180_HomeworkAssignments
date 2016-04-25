package com.example.group26.stayintouchwithfragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by carlosrosario on 4/23/16.
 */
public class NavigationDrawerItemAdapter extends ArrayAdapter<NavigationDrawerItem> {

    List<NavigationDrawerItem> mData;
    Context mContext;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        NavigationDrawerItem navigationDrawerItem = mData.get(position);

        if(convertView == null) {
            // Set the profile image/username for first row in navigation drawer
            if (position == 0) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.drawer_row_item_custom_layout1, null);

                // Set profile picture
                ImageView profileImageView = (ImageView)convertView.findViewById(R.id.navigationProfileImageView);
                String base64ProfilePicture = navigationDrawerItem.getBase64ProfilePicture();
                if(base64ProfilePicture != null && !base64ProfilePicture.isEmpty()){
                    byte[] decodedImageByteArray = Base64.decode(base64ProfilePicture, Base64.DEFAULT);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(decodedImageByteArray, 0, decodedImageByteArray.length);
                    profileImageView.setImageBitmap(decodedImage);
                }

                // Set name
                TextView nameTextView = (TextView)convertView.findViewById(R.id.navigationProfileNameTextView);
                nameTextView.setText(navigationDrawerItem.getName());
            }
            // Set the names of the various fragments for all other rows in the navigation drawer
            else {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.drawer_row_item_custom_layout2, null);

                // Set fragment item name
                TextView fragmentNameTextView = (TextView)convertView.findViewById(R.id.navigationFragmentNameTextView);
                fragmentNameTextView.setText(navigationDrawerItem.getNavigationItem());
            }
        }

        return convertView;
    }

    public NavigationDrawerItemAdapter(Context context, int resource, List<NavigationDrawerItem> objects){
        super(context, resource, objects);
        this.mData = objects;
        this.mContext = context;
    }
}
