package com.example.group26.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class EditProfileActivity extends AppCompatActivity {

    User currentlyLoggedInUser;
    EditText fullNameEditText;
    EditText emailEditText;
    EditText phoneNumberEditText;
    EditText passwordEditText;
    ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        fullNameEditText = (EditText)findViewById(R.id.editProfileNameText);
        emailEditText = (EditText)findViewById(R.id.editProfileEmailText);
        phoneNumberEditText = (EditText)findViewById(R.id.editProfilePhomeNumber);
        passwordEditText = (EditText)findViewById(R.id.editProfilePassword);
        profileImageView = (ImageView)findViewById(R.id.editProfileImageView);

        if(getIntent() != null && getIntent().getExtras()  != null){
            currentlyLoggedInUser = (User)getIntent().getExtras().getSerializable("USER");
        }

        fullNameEditText.setText(currentlyLoggedInUser.getFullName());
        emailEditText.setText(currentlyLoggedInUser.getEmail());
        phoneNumberEditText.setText(currentlyLoggedInUser.getPhoneNumber());
        passwordEditText.setText(currentlyLoggedInUser.getPassword());

        byte[] decodedImageByteArray = Base64.decode(currentlyLoggedInUser.getBase64Picture(), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(decodedImageByteArray, 0, decodedImageByteArray.length);
        profileImageView.setImageBitmap(decodedImage);


        // Update Button logic
        findViewById(R.id.editProfileUpdatebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Cancel Button logic
        findViewById(R.id.editProfileCancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
