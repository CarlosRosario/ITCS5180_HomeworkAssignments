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
    EditText confirmedPasswordEditText;


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
        confirmedPasswordEditText = (EditText)findViewById(R.id.signupActivityPasswordConfirmationEditText);

        // Sign Up Button logic
        //noinspection ConstantConditions
        findViewById(R.id.signupActivitySignUpButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String fullName = fullNameEditText.getText().toString();
                final String signUpEmail = emailEditText.getText().toString();
                final String phoneNumber = phoneNumberEditText.getText().toString();
                final String password = passwordEditText.getText().toString();
                String confirmPassword = confirmedPasswordEditText.getText().toString();

                // Make sure all fields are populated
                if(fullName == null || fullName.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(signUpEmail == null || signUpEmail.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(phoneNumber == null || phoneNumber.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(password == null || password.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(confirmPassword == null || confirmPassword.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Please confirm your password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Make sure that password/confirm password fields are the same
                if(password.equals(confirmPassword)){
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
                else {
                    Toast.makeText(SignUpActivity.this, "passwords do not match", Toast.LENGTH_SHORT).show();
                }
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
}
