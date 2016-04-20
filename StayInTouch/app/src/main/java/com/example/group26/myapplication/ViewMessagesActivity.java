package com.example.group26.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by meredithbrowne on 4/17/16.
 */


public class ViewMessagesActivity extends AppCompatActivity {
    MyApplication applicationContext;
    List<Message> messageList = new ArrayList<Message>();
    ListView listView;
    MessageAdapter messageAdapter;
    User selectedContact;
    String myName;
    EditText messageTextEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_messages);
        applicationContext = (MyApplication) getApplicationContext();
        final Firebase firebase = applicationContext.getFireBase();
        messageTextEditText = (EditText)findViewById(R.id.viewMessagesMessageEditText);

        if (getIntent() != null && getIntent().getExtras() != null) {
            selectedContact = (User)getIntent().getExtras().getSerializable("CONTACT");
            myName = getIntent().getExtras().getString("MYNAME");
        } else {
            Toast.makeText(ViewMessagesActivity.this, "Input required.", Toast.LENGTH_SHORT);
        }

        messageAdapter = new MessageAdapter(ViewMessagesActivity.this, R.layout.view_messages_activity_custom_listview_layout, messageList, myName);
        listView = (ListView) findViewById(R.id.viewMessagesListView);
        listView.setAdapter(messageAdapter);
        messageAdapter.setNotifyOnChange(true);

        //noinspection ConstantConditions
        findViewById(R.id.viewMessagesSendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(messageTextEditText.getText() != null){
                    //firebase.child("Messages")
                    String messageText = messageTextEditText.getText().toString();
                    if(messageText != null && !messageText.isEmpty()){

                        if(messageText.length() > 140){
                            Toast.makeText(ViewMessagesActivity.this, "Please enter 140 characters or less", Toast.LENGTH_SHORT).show();
                        }

                        Message newMessage = new Message();
                        newMessage.setMessage_text(messageText);
                        newMessage.setEmail(selectedContact.getEmail());
                        newMessage.setMessage_read(false);
                        newMessage.setReceiver(selectedContact.getFullName());
                        newMessage.setSender(myName);
                        newMessage.setTimeStamp(new Date().toString());

                        firebase.child("Messages").push().setValue(newMessage);
                        messageAdapter.add(newMessage);
                    }
                    else {
                        Toast.makeText(ViewMessagesActivity.this, "Please enter some text to send", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(ViewMessagesActivity.this, "Please enter some text to send", Toast.LENGTH_SHORT).show();
                }
                messageTextEditText.setText("");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viewmessages_activity_custom_action_bar, menu);
        return true;
    }

    public void viewContact(MenuItem menuItem){
        Intent viewContactActivityIntent = new Intent(ViewMessagesActivity.this, ViewContactActivity.class);
        viewContactActivityIntent.putExtra("CONTACT", selectedContact);
        startActivity(viewContactActivityIntent);
    }

    public void callContact(MenuItem menuItem){

        String selectedPhoneNumber = selectedContact.getPhoneNumber();
        Log.d("test", "clicked phone is: " + selectedPhoneNumber);

        int hasPhonePermissions = ContextCompat.checkSelfPermission(ViewMessagesActivity.this, Manifest.permission.CALL_PHONE);
        if(hasPhonePermissions != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ViewMessagesActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 100);
            return;
        }

        Intent phoneCallIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + selectedPhoneNumber));
        try {
            startActivity(phoneCallIntent);
        }
        catch(SecurityException e){
            Log.d("error", e.getMessage());
            Log.d("error", e.getStackTrace().toString());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent phoneCallIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + selectedContact.getPhoneNumber()));
                    try {
                        startActivity(phoneCallIntent);
                    }
                    catch(SecurityException e){
                        Log.d("error", e.getMessage());
                        Log.d("error", e.getStackTrace().toString());
                    }

                } else {
                    Toast.makeText(ViewMessagesActivity.this, "CALL PHONE DENIED", Toast.LENGTH_SHORT).show();

                }
                return;
            }
        }
    }


}
