package com.example.group26.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {

    MyApplication applicationContext;
    User currentlyLoggedInUser;
    String currentlyLoggedInUserUniqueKey;
    EditText fullNameEditText;
    EditText emailEditText;
    EditText phoneNumberEditText;
    EditText passwordEditText;
    ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        applicationContext = (MyApplication)getApplicationContext();
        final Firebase firebase = applicationContext.getFireBase();

        fullNameEditText = (EditText)findViewById(R.id.editProfileNameText);
        emailEditText = (EditText)findViewById(R.id.editProfileEmailText);
        phoneNumberEditText = (EditText)findViewById(R.id.editProfilePhomeNumber);
        passwordEditText = (EditText)findViewById(R.id.editProfilePassword);
        profileImageView = (ImageView)findViewById(R.id.editProfileImageView);

        if(getIntent() != null && getIntent().getExtras()  != null){
            currentlyLoggedInUser = (User)getIntent().getExtras().getSerializable("USER");
            currentlyLoggedInUserUniqueKey = getIntent().getExtras().getString("USERUNIQUEKEY");
        }

        fullNameEditText.setText(currentlyLoggedInUser.getFullName());
        emailEditText.setText(currentlyLoggedInUser.getEmail());
        phoneNumberEditText.setText(currentlyLoggedInUser.getPhoneNumber());
        passwordEditText.setText(currentlyLoggedInUser.getPassword());

        byte[] decodedImageByteArray = Base64.decode(currentlyLoggedInUser.getBase64Picture(), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(decodedImageByteArray, 0, decodedImageByteArray.length);
        profileImageView.setImageBitmap(decodedImage);


        // Change Profile Image logic
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        // Update Button logic
        // noinspection ConstantConditions
        findViewById(R.id.editProfileUpdatebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newEmail = emailEditText.getText().toString();
                final String newPassword = passwordEditText.getText().toString();

                final String currentEmail = currentlyLoggedInUser.getEmail();
                final String currentPassword = currentlyLoggedInUser.getPassword();

                if(emailEditText.getText() != null){
                    //final String newEmail = emailEditText.getText().toString();

                    if(newEmail.equals(currentEmail)){
                        // Then "newEmail" isn't a new email
                        update(firebase, newEmail, newPassword);

                        // Still need to check if there is a new password
                        if(passwordEditText.getText() != null){
                            if(!newPassword.equals(currentPassword)){

                                firebase.changePassword(currentEmail, currentPassword, newPassword, new Firebase.ResultHandler() {
                                    @Override
                                    public void onSuccess() {
                                        Toast.makeText(EditProfileActivity.this, "Password successfully updated", Toast.LENGTH_SHORT).show();
                                        currentlyLoggedInUser.setPassword(newPassword);
                                        reAuth(firebase, currentEmail, newPassword);

                                        update(firebase, newEmail, newPassword);

                                    }

                                    @Override
                                    public void onError(FirebaseError firebaseError) {
                                        Log.d("testing", firebaseError.getMessage());
                                        Log.d("testing", firebaseError.getDetails());
                                        Log.d("testing", firebaseError.getCode() + "");
                                        Toast.makeText(EditProfileActivity.this, "Password failed to update", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }


                    }
                    else {
                        // "newEmail" is a new email

                        firebase.changeEmail(currentEmail, currentPassword, newEmail, new Firebase.ResultHandler() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(EditProfileActivity.this, "Email successfully updated", Toast.LENGTH_SHORT).show();
                                reAuth(firebase, newEmail, currentPassword);

                                // Still need to check if there is a new password
                                if (passwordEditText.getText() != null) {
                                    final String newPassword = passwordEditText.getText().toString();
                                    if (!newPassword.equals(currentPassword)) {
                                        currentlyLoggedInUser.setPassword(newPassword);
                                        firebase.changePassword(currentEmail, currentPassword, newPassword, new Firebase.ResultHandler() {
                                            @Override
                                            public void onSuccess() {
                                                Toast.makeText(EditProfileActivity.this, "Password successfully updated", Toast.LENGTH_SHORT).show();
                                                reAuth(firebase, newEmail, newPassword);
                                            }

                                            @Override
                                            public void onError(FirebaseError firebaseError) {
                                                Log.d("testing", firebaseError.getMessage());
                                                Log.d("testing", firebaseError.getDetails());
                                                Log.d("testing", firebaseError.getCode() + "");
                                                Toast.makeText(EditProfileActivity.this, "Password failed to update", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                                update(firebase, newEmail, newPassword);
                            }

                            @Override
                            public void onError(FirebaseError firebaseError) {
                                Log.d("testing", firebaseError.getMessage());
                                Log.d("testing", firebaseError.getDetails());
                                Log.d("testing", firebaseError.getCode() + "");
                                Toast.makeText(EditProfileActivity.this, "Email failed to update", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                Toast.makeText(EditProfileActivity.this, "Saved Successfully", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        // Cancel Button logic
        // noinspection ConstantConditions
        findViewById(R.id.editProfileCancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            Uri chosenImageUri = data.getData();

            Bitmap mBitmap = null;
            try{
                mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), chosenImageUri);
                profileImageView.setImageBitmap(mBitmap);
            } catch(IOException e){

            }
        }
    }

    public void update(Firebase firebase, String email, String password){
        if(phoneNumberEditText.getText() != null){
            String newPhone = phoneNumberEditText.getText().toString();
            if(newPhone.equals(currentlyLoggedInUser.getPhoneNumber())){
                // Then "newPhone" isn't a new phone
            }
            else {
                // "newPhone" is a new phone
                currentlyLoggedInUser.setPhoneNumber(newPhone);
            }
        }

        if(fullNameEditText.getText() != null){
            String newFullName = fullNameEditText.getText().toString();
            if(newFullName.equals(currentlyLoggedInUser.getFullName())){
                // Then "newFullName" isn't a new phone
            }
            else {
                // "newFullName" is a new phone
                currentlyLoggedInUser.setFullName(newFullName);
            }
        }
        currentlyLoggedInUser.setBase64Picture(encodeDummyProfilePic(((BitmapDrawable) profileImageView.getDrawable()).getBitmap()));
        currentlyLoggedInUser.setEmail(email);
        currentlyLoggedInUser.setPassword(password);
        firebase.child("users").child(currentlyLoggedInUserUniqueKey).setValue(currentlyLoggedInUser);
        //Toast.makeText(EditProfileActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
    }

    public void reAuth(Firebase firebase, String email, String password){
        // Re-Authenticate
        // Make sure to re-authenticate with newest credentials.
        firebase.unauth();
        Log.d("re-auth", "re-auth with: " + email);
        Log.d("re-auth", "re-auth with: " + password);
        firebase.authWithPassword(email, password,
                new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Log.d("re-auth", "re-auth successful");
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError error) {
                        Log.d("re-auth", error.getDetails());
                        Log.d("re-auth", error.getMessage());
                        Log.d("re-auth", "re-auth unsuccessful");
                    }
                });
    }

    public String encodeDummyProfilePic(Bitmap bitmap){
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        final byte[] image=stream.toByteArray();
        final String base64DummyImage = Base64.encodeToString(image, 0);
        return base64DummyImage;
    }
}
