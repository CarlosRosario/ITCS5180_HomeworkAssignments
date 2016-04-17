package com.example.group26.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    MyApplication applicationContext;
    EditText fullNameEditText;
    EditText emailEditText;
    EditText phoneNumberEditText;
    EditText passwordEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        applicationContext = (MyApplication)getApplicationContext();
        final Firebase firebase = applicationContext.getFireBase();
        fullNameEditText = (EditText)findViewById(R.id.signupActivityNameEditText);
        emailEditText = (EditText)findViewById(R.id.signupActivityEmailEditText);
        phoneNumberEditText = (EditText)findViewById(R.id.signupActivityPhoneNumberEditText);
        passwordEditText = (EditText)findViewById(R.id.signupActivityPasswordEditText);

        // Sign Up Button logic
        //noinspection ConstantConditions
        findViewById(R.id.signupActivitySignUpButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String fullName = fullNameEditText.getText().toString();
                final String signUpEmail = emailEditText.getText().toString();
                final String phoneNumber = phoneNumberEditText.getText().toString();
                final String password = passwordEditText.getText().toString();

                // save user as registered user and to JSON store
                firebase.createUser(signUpEmail, password, new Firebase.ValueResultHandler<Map<String,Object>>(){
                    @Override
                    public void onSuccess(Map<String, Object> stringObjectMap) {

                        User newUser = new User();
                        newUser.setFullName(fullName);
                        newUser.setEmail(signUpEmail);
                        newUser.setPhoneNumber(phoneNumber);
                        newUser.setPassword(password);
                        newUser.setBase64Picture(applicationContext.getDummyProfilePic());

                        //firebase.child("users").child("username").push().setValue(newUser);
                        firebase.child("users").push().setValue(newUser);
                        Toast.makeText(SignUpActivity.this, "Successfully created new account", Toast.LENGTH_SHORT).show();
                        finish(); // bring us back to the login activity
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        int errorCode = firebaseError.getCode();

                        switch (errorCode) {
                            case FirebaseError.EMAIL_TAKEN:
                                Toast.makeText(SignUpActivity.this, "Email is already taken", Toast.LENGTH_SHORT).show();
                                break;

                            default:
                                Toast.makeText(SignUpActivity.this, firebaseError.toString(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
            }
        });

        // Cancel Button logic
        //noinspection ConstantConditions
        findViewById(R.id.signupActivityCancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public String retrieveDummyPicAsBase64String(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dummy);
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        final byte[] image=stream.toByteArray();
        System.out.println("byte array:"+image);
        final String img_str = Base64.encodeToString(image, 0);
        return img_str;
    }
}
