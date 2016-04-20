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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.Query;
import com.squareup.picasso.Picasso;

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
    Firebase firebase;
    String phoneNumberToDial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);
        applicationContext = (MyApplication)getApplicationContext();
        firebase = applicationContext.getFireBase();

        firebase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final String currentlyLoggedInUserEmail = firebase.getAuth().getProviderData().get("email").toString();

                User me = null;
                for (DataSnapshot userSnapShot : dataSnapshot.getChildren()) {
                    User contact = userSnapShot.getValue(User.class);
                    if(!contact.getEmail().equals(currentlyLoggedInUserEmail)) {
                        contacts.add(contact);
                    }
                    else {
                        me = contact;
                    }
                }

                final String myName = me.getFullName();
                final String myEmail = me.getEmail();

                // Create Listview with custom adapter
                Log.d("test", "about to create adapter");
                contactAdapter = new ContactAdapter(ConversationsActivity.this, R.layout.conversations_activity_custom_listview_layout, contacts, myEmail);
                listView = (ListView) findViewById(R.id.conversationsActivityListView);
                listView.setAdapter(contactAdapter);
                contactAdapter.setNotifyOnChange(true);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                        Log.d("testing3", contacts.get(position).getEmail());
                        Query queryRef = firebase.child("Messages").orderByChild("email").equalTo(myEmail);
                        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                                    String key = messageSnapshot.getKey();
                                    final Message contactMessage = messageSnapshot.getValue(Message.class);
                                    contactMessage.setMessage_read(true);
                                    firebase.child("Messages").child(key).setValue(contactMessage);

                                    ImageView redBubble = (ImageView)view.findViewById(R.id.conversationsActivityRedBubbleImageView);
                                    redBubble.setVisibility(View.INVISIBLE);


                                }
                            }
                            @Override
                            public void onCancelled (FirebaseError firebaseError){


                            }
                        });


                        User selectedContact = contacts.get(position);
                        Intent viewMessasgesActivityIntent = new Intent(ConversationsActivity.this, ViewMessagesActivity.class);
                        viewMessasgesActivityIntent.putExtra("CONTACT", selectedContact);
                        viewMessasgesActivityIntent.putExtra("MYNAME", myName);
                        startActivity(viewMessasgesActivityIntent);
                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent phoneCallIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumberToDial));
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

    public void setPermissionsPhoneNumber(String permissionsPhoneNumber){
        this.phoneNumberToDial = permissionsPhoneNumber;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.conversations_activity_custom_action_bar, menu);
        return true;
    }

    public void editProfile(MenuItem menuItem){

        final String currentlyLoggedInUserEmail = firebase.getAuth().getProviderData().get("email").toString();
        Log.d("re-auth", "current logged in email: " + currentlyLoggedInUserEmail);

        Query queryRef = firebase.child("users").orderByChild("email").equalTo(currentlyLoggedInUserEmail);
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap data = (HashMap) dataSnapshot.getValue();

                // this loop only runs one time
                for (Object key : data.keySet()) {
                    HashMap innerData = (HashMap) data.get(key);

                    String uniqueKeyForUser = key.toString();
                    String currentlyLoggedInPassword = innerData.get("password").toString();
                    String currentlyLoggedInPhoneNumber = innerData.get("phoneNumber").toString();
                    String currentlyLoggedInFullName = innerData.get("fullName").toString();
                    String currentlyLoggedInProfilePicture = innerData.get("base64Picture").toString();

                    User user = new User();
                    user.setBase64Picture(currentlyLoggedInProfilePicture);
                    user.setEmail(currentlyLoggedInUserEmail);
                    user.setPassword(currentlyLoggedInPassword);
                    user.setFullName(currentlyLoggedInFullName);
                    user.setPhoneNumber(currentlyLoggedInPhoneNumber);

                    Intent editProfileActivityIntent = new Intent(ConversationsActivity.this, EditProfileActivity.class);
                    editProfileActivityIntent.putExtra("USER", user);
                    editProfileActivityIntent.putExtra("USERUNIQUEKEY", uniqueKeyForUser);
                    startActivity(editProfileActivityIntent);
                    break;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    public void logout(MenuItem menuItem){
        Firebase firebase = applicationContext.getFireBase();
        firebase.unauth();

        Intent loginActivityIntent = new Intent(ConversationsActivity.this, LoginActivity.class);
        startActivity(loginActivityIntent);
        finish();
    }
}
