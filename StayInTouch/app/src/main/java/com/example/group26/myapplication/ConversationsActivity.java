package com.example.group26.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.firebase.client.Firebase;

public class ConversationsActivity extends AppCompatActivity {

    MyApplication applicationContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);
        applicationContext = (MyApplication)getApplicationContext();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.conversations_activity_custom_action_bar, menu);
        return true;
    }

    public void editProfile(MenuItem menuItem){

    }

    public void logout(MenuItem menuItem){
        Firebase firebase = applicationContext.getFireBase();
        firebase.unauth();

        Intent loginActivityIntent = new Intent(ConversationsActivity.this, LoginActivity.class);
        startActivity(loginActivityIntent);
        finish();
    }
}
