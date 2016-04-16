package com.example.group26.myapplication;

import android.content.Intent;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    MyApplication applicationContext;
    EditText emailEditText;
    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        applicationContext = (MyApplication)getApplicationContext();
        final Firebase firebase = applicationContext.getFireBase();
        emailEditText = (EditText)findViewById(R.id.loginActivityEmailEditText);
        passwordEditText = (EditText)findViewById(R.id.loginActivityPasswordEditText);

        // Check if there is a current user session
        boolean isThereACurrentUserSession = isCurrentUserAuthenticated(firebase);
        if(isThereACurrentUserSession){
            // Start Conversation activity, and finish the login activity
            navigateToConversationsActivity(true);
        }

        // Login Button logic
        findViewById(R.id.loginActivityLoginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebase.authWithPassword(emailEditText.getText().toString(), passwordEditText.getText().toString(),
                        new Firebase.AuthResultHandler() {
                            @Override
                            public void onAuthenticated(AuthData authData) {
                                // Authentication just completed successfully :)
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("provider", authData.getProvider());
                                if(authData.getProviderData().containsKey("displayName")) {
                                    map.put("displayName", authData.getProviderData().get("displayName").toString());
                                }
                                firebase.child("users").child(authData.getUid()).setValue(map);

                                Intent ConversationsActivityIntent = new Intent(LoginActivity.this, ConversationsActivity.class);
                                startActivity(ConversationsActivityIntent);
                            }
                            @Override
                            public void onAuthenticationError(FirebaseError error) {
                                Toast.makeText(LoginActivity.this, "Unsuccessful authentication", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


        // Create New Account Button logic
        findViewById(R.id.loginActivityNewAccountButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }


    public boolean isCurrentUserAuthenticated(Firebase firebase){

        AuthData authData = firebase.getAuth();

        if(authData != null){
            return true;
        }
        else {
            return false;
        }
    }

    public void navigateToConversationsActivity(boolean finishLoginActivity){
        Intent conversationsActivityIntent = new Intent(LoginActivity.this, ConversationsActivity.class);
        startActivity(conversationsActivityIntent);

        if(finishLoginActivity){
            finish();
        }
    }
}
