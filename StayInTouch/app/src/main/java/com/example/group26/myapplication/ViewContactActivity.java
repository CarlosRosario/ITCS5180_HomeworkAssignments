package com.example.group26.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewContactActivity extends AppCompatActivity {

    User selectedContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);

        if(getIntent() != null && getIntent().getExtras() != null){
            selectedContact = (User)getIntent().getExtras().getSerializable("CONTACT");
        }

        TextView fullNameAtTop = (TextView)findViewById(R.id.viewContactFirstAndLastName);
        ImageView profileImage = (ImageView)findViewById(R.id.viewContactImageView);
        TextView nameAtMiddle = (TextView)findViewById(R.id.viewContactNameText);
        TextView phoneNumber = (TextView)findViewById(R.id.viewContactPhoneNumberText);
        TextView email = (TextView)findViewById(R.id.viewContactEmailIdText);

        fullNameAtTop.setText(selectedContact.getFullName());
        nameAtMiddle.setText(" " + selectedContact.getFullName());
        phoneNumber.setText(" " + selectedContact.getPhoneNumber());
        email.setText(" " + selectedContact.getEmail());

        byte[] decodedImageByteArray = Base64.decode(selectedContact.getBase64Picture(), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(decodedImageByteArray, 0, decodedImageByteArray.length);
        profileImage.setImageBitmap(decodedImage);

    }
}
