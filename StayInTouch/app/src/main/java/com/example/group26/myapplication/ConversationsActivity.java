package com.example.group26.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConversationsActivity extends AppCompatActivity {

    MyApplication applicationContext;
    List<User> contacts = new ArrayList<User>();
    ListView listView;
    ContactAdapter contactAdapter;
    final int REQUEST_CODE_ASK_PERMISSIONS = 100;

    String phoneNumberToDial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);
        applicationContext = (MyApplication)getApplicationContext();
        Firebase firebase = applicationContext.getFireBase();


        firebase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapShot : dataSnapshot.getChildren()) {
                    User contact = userSnapShot.getValue(User.class);
                    Log.d("test", contact.getFullName());
                    Log.d("test", contact.getEmail());
                    contacts.add(contact);
                }

                // Create Listview with custom adapter
                Log.d("test", "about to create adapter");
                contactAdapter = new ContactAdapter(ConversationsActivity.this, R.layout.conversations_activity_custom_listview_layout, contacts);
                listView = (ListView) findViewById(R.id.conversationsActivityListView);
                listView.setAdapter(contactAdapter);
                contactAdapter.setNotifyOnChange(true);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

//        final Map<String, List<Message>> messagesByEmail = new HashMap<String, List<Message>>();
//        firebase.child("messages").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                List<Message> currentMessagesList = new ArrayList<Message>();
//                String previousEmail = "";
//                String currentEmail = "";
//                for (DataSnapshot messageSnapShot : dataSnapshot.getChildren()) {
//                    Message message = messageSnapShot.getValue(Message.class);
//
//                    // Store current messages associated email in 'currentEmail'
//                    currentEmail = message.getEmail();
//
//                    // If there isn't a previousEmail yet stored then that means we are on first iteration
//                    if (previousEmail == null || previousEmail.isEmpty()) {
//                        currentMessagesList.add(message);
//                        previousEmail = message.getEmail();
//                    } else if (previousEmail.equals(currentEmail)) {
//                        currentMessagesList.add(message);
//                    } else {
//                        messagesByEmail.put(previousEmail, currentMessagesList);
//                        currentMessagesList = new ArrayList<Message>();
//                        currentMessagesList.add(message);
//                        previousEmail = currentEmail;
//                    }
//                }
//
//                messagesByEmail.put(currentEmail, currentMessagesList);
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });




    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent phoneCallIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:8642855208"));
                    try {
                        startActivity(phoneCallIntent);
                    }
                    catch(SecurityException e){
                        Log.d("error", e.getMessage());
                        Log.d("error", e.getStackTrace().toString());
                    }

                } else {
                    Toast.makeText(ConversationsActivity.this, "CALL PHONE DENIED", Toast.LENGTH_SHORT).show();

                }
                return;
            }
        }
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
