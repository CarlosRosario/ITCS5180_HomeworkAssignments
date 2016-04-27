package com.example.group26.stayintouchwithfragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener
, SignUpFragment.OnFragmentInteractionListener, ContactsFragment.OnFragmentInteractionListener, ConversationFragment.OnFragmentInteractionListener{

    String firebaseUrl = "https://stayintouchfrag-5180.firebaseio.com/";
    String dummyProfilePic;
    FirebaseSerialize firebase;
    String phoneNumberToDial;
    User currentlyLoggedInUser;

    private final String LOGINFRAGMENTTAG = "LOGIN";
    private final String SIGNUPFRAGMENTTAG = "SIGNUP";
    private final String CONVERSATIONFRAGMENTTAG = "CONVERSATION";
    private final String CONTACTSFRAGMENTTAG = "CONTACTS";
    private final String ARCHIVEDCONVERSATIONSFRAGMENTTAG = "ARCHIVED";
    private final String SETTINGSFRAGMENTTAG = "SETTINGS";
    private final String VIEWMESSAGESFRAGMENTTAG = "VIEWMESSAGES";
    private final String VIEWCONTACTFRAGMENTTAG = "VIEWCONTACT";

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationDrawerItemAdapter navigationDrawerItemAdapter;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mFragmentTitles;
    private List<NavigationDrawerItem> navigationDrawerItemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        Firebase.setAndroidContext(MainActivity.this);
        firebase = new FirebaseSerialize(firebaseUrl);

        // Set up Navigation Drawer
        initializeNavigationDrawer();

        // Initialize the login fragment + pass it a reference to the firebase object
        LoginFragment loginFragment = new LoginFragment();
        Bundle loginFragmentArguments = new Bundle();
        loginFragmentArguments.putSerializable("Firebase", firebase);
        loginFragment.setArguments(loginFragmentArguments);
        getFragmentManager().beginTransaction().add(R.id.mainActivityContainer, loginFragment, LOGINFRAGMENTTAG).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStack();
            setTitle("Stay in Touch");
        }
        else {
            super.onBackPressed();
        }
    }

    private void initializeNavigationDrawer(){

        // Navigation Drawer Initialization
        mTitle = mDrawerTitle = getTitle();
        mFragmentTitles = getResources().getStringArray(R.array.navigationDrawerItems);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerList = (ListView)findViewById(R.id.left_drawer);

        // Set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // Set up the drawer's list view with items and click listener
        navigationDrawerItemsList = new ArrayList<NavigationDrawerItem>();
        NavigationDrawerItem firstItem = new NavigationDrawerItem();
        firstItem.setBase64ProfilePicture(getDummyProfilePic());
        firstItem.setName("test name");
        navigationDrawerItemsList.add(firstItem);
        for(String title : mFragmentTitles){
            NavigationDrawerItem item = new NavigationDrawerItem();
            item.setNavigationItem(title);
            navigationDrawerItemsList.add(item);
        }
        navigationDrawerItemAdapter = new NavigationDrawerItemAdapter(MainActivity.this, 0 , navigationDrawerItemsList);
        mDrawerList.setAdapter(navigationDrawerItemAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        navigationDrawerItemAdapter.setNotifyOnChange(true);

        // Enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);

        // ActionBarDrawerToggle ties together the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }



    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Toast.makeText(MainActivity.this, "Position: " + position + " clicked", Toast.LENGTH_SHORT).show();
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // Update the main content by replacing fragments
        switch (position) {
            case 0:
                // ignore
                break;

            case 1:
                // Load contacts fragment if it's not already loaded
                ContactsFragment testContactsFragment = (ContactsFragment)getFragmentManager().findFragmentByTag(CONTACTSFRAGMENTTAG);
                if(testContactsFragment != null && testContactsFragment.isVisible()){
                    // do nothing - the Contacts are already showing
                }
                else {
                    // Make sure that there is a user logged in before navigating to the contacts fragment
                    // this was not explicity mentioned in the assignment pdf but, it doesn't make sense
                    // to list contacts when the app is not even logged into.
                    if(isCurrentUserAuthenticated(firebase)){
                        ContactsFragment contactFragment = new ContactsFragment();
                        Bundle contactFragmentArguments = new Bundle();
                        contactFragmentArguments.putSerializable("Firebase", firebase);
                        contactFragment.setArguments(contactFragmentArguments);
                        getFragmentManager().beginTransaction().replace(R.id.mainActivityContainer, contactFragment, CONTACTSFRAGMENTTAG).addToBackStack(null).commit();
                        closeNavigationDrawer(position, CONTACTSFRAGMENTTAG);
                    }
                    // If there isn't a user logged into the app while trying to navigate to the contacts fragment, display a warning toast.
                    else {
                        Toast.makeText(MainActivity.this, "You are not currently logged in, after logging in you can view your contacts", Toast.LENGTH_SHORT).show();
                    }

                }
                break;

            case 2:
                // Load all conversations into conversation fragment
                ConversationFragment testConversationsFragment = (ConversationFragment)getFragmentManager().findFragmentByTag(CONVERSATIONFRAGMENTTAG);
                if(testConversationsFragment != null && testConversationsFragment.isVisible()){
                    // do nothing - the Conversations are already showing
                }
                else {
                    getFragmentManager().beginTransaction().replace(R.id.mainActivityContainer, new ConversationFragment(), CONVERSATIONFRAGMENTTAG).commit();
                    closeNavigationDrawer(position, CONVERSATIONFRAGMENTTAG);

                }
                break;


            case 3:
                // Load only the archived conversations into conversation fragment
                ConversationFragment testArchivedConversationsFragment = (ConversationFragment)getFragmentManager().findFragmentByTag(CONVERSATIONFRAGMENTTAG);
                if(testArchivedConversationsFragment != null && testArchivedConversationsFragment.isVisible()){
                    // do nothing - the Conversations are already showing
                }
                else {
                    getFragmentManager().beginTransaction().replace(R.id.mainActivityContainer, new ConversationFragment(), CONVERSATIONFRAGMENTTAG).commit();
                    closeNavigationDrawer(position, ARCHIVEDCONVERSATIONSFRAGMENTTAG);
                }

                break;

            case 4:
                EditProfileFragment testEditProfileFragment = (EditProfileFragment)getFragmentManager().findFragmentByTag(SETTINGSFRAGMENTTAG);
                if(testEditProfileFragment != null && testEditProfileFragment.isVisible()){
                    // do nothing - the edit profile fragment is already showing
                }
                else {
                    getFragmentManager().beginTransaction().replace(R.id.mainActivityContainer, new EditProfileFragment(), SETTINGSFRAGMENTTAG);
                    closeNavigationDrawer(position, SETTINGSFRAGMENTTAG);
                }
                break;


            case 5:
                LoginFragment testLoginFragment = (LoginFragment)getFragmentManager().findFragmentByTag(LOGINFRAGMENTTAG);
                if(testLoginFragment != null && testLoginFragment.isVisible()){
                    // do nothing - user is not currently logged in as the login fragment is currently showing.
                    Toast.makeText(MainActivity.this, "You are already logged out", Toast.LENGTH_SHORT).show();
                }
                else {
                    firebase.unauth();
                    LoginFragment loginFragment = new LoginFragment();
                    Bundle loginFragmentArguments = new Bundle();
                    loginFragmentArguments.putSerializable("Firebase", firebase);
                    loginFragment.setArguments(loginFragmentArguments);
                    getFragmentManager().beginTransaction().replace(R.id.mainActivityContainer, loginFragment, LOGINFRAGMENTTAG).commit();
                    closeNavigationDrawer(position, LOGINFRAGMENTTAG);
                }

                break;

            case 6:
                finish();
                break;
        }
    }

    public void closeNavigationDrawer(int position, String title){
        // Update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(title);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    public void setPermissionsPhoneNumber(String phoneNumberToDial){
        this.phoneNumberToDial = phoneNumberToDial;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
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
                    Toast.makeText(MainActivity.this, "CALL PHONE DENIED", Toast.LENGTH_SHORT).show();

                }
                return;
            }
        }
    }

    public String getDummyProfilePic(){
        if(dummyProfilePic == null || dummyProfilePic.isEmpty()){
            dummyProfilePic = encodeDummyProfilePic();
        }

        return dummyProfilePic;
    }

    public String encodeDummyProfilePic(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dummy);
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        final byte[] image=stream.toByteArray();
        final String base64DummyImage = Base64.encodeToString(image, 0);
        return base64DummyImage;
    }

    @Override
    public void navigateToConversationsFragment() {

        // Enable the navigation drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(CONVERSATIONFRAGMENTTAG);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        // User has successfully logged in, lets try to update the navigation drawer profile image + user name
        // First, lets get the email of the currently logged in user.
        String currentlyLoggedInUserEmail = firebase.getAuth().getProviderData().get("email").toString();

        // Next, lets use the currently logged in user email to retrieve the corresponding User object.
        // The RetrieveCurrentUserValueEventListener will then perform all the work necessary to update
        // the navigation drawer with the users profile image/user name.
        Firebase userRef = firebase.child("users").child(currentlyLoggedInUserEmail.toLowerCase().replaceAll("[@.]", "+"));
        userRef.addListenerForSingleValueEvent(new RetrieveCurrentUserValueEventListener());

//        // Create conversation fragment
//        ConversationFragment conversationFragment = new ConversationFragment();
//        Bundle conversationFragmentArguments = new Bundle();
//        conversationFragmentArguments.putSerializable("Firebase", firebase);
//        conversationFragmentArguments.putSerializable("CurrentLoggedInUser", currentlyLoggedInUser);
//        conversationFragment.setArguments(conversationFragmentArguments);
//        getFragmentManager().beginTransaction().replace(R.id.mainActivityContainer, conversationFragment, CONVERSATIONFRAGMENTTAG).commit();
    }

    private class RetrieveCurrentUserValueEventListener implements ValueEventListener{
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            currentlyLoggedInUser = dataSnapshot.getValue(User.class);
            Log.d("user", "current user name: " + currentlyLoggedInUser.getFullName());
            Log.d("user", "current user password: " + currentlyLoggedInUser.getPassword());
            Log.d("user", "current user phone: " + currentlyLoggedInUser.getPhoneNumber());
            Log.d("user", "current user email: " + currentlyLoggedInUser.getEmail());

            // Go ahead and update the first row in the navigation drawer ... I think there is a better way to do this though.
            NavigationDrawerItem newNavigationDrawerItem = new NavigationDrawerItem();
            newNavigationDrawerItem.setName(currentlyLoggedInUser.getFullName());
            newNavigationDrawerItem.setBase64ProfilePicture(currentlyLoggedInUser.getBase64Picture());
            navigationDrawerItemsList.remove(0);
            navigationDrawerItemsList.add(0, newNavigationDrawerItem);
            navigationDrawerItemAdapter = new NavigationDrawerItemAdapter(MainActivity.this, 0, navigationDrawerItemsList);
            mDrawerList.setAdapter(navigationDrawerItemAdapter);
            navigationDrawerItemAdapter.notifyDataSetChanged(); // I'm not sure that this line is necessary

            // Create conversation fragment
            ConversationFragment conversationFragment = new ConversationFragment();
            Bundle conversationFragmentArguments = new Bundle();
            conversationFragmentArguments.putSerializable("Firebase", firebase);
            conversationFragmentArguments.putSerializable("CurrentLoggedInUser", currentlyLoggedInUser);
            conversationFragment.setArguments(conversationFragmentArguments);
            getFragmentManager().beginTransaction().replace(R.id.mainActivityContainer, conversationFragment, CONVERSATIONFRAGMENTTAG).commit();
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    }

    @Override
    public void navigateToSignUpFragment() {
        SignUpFragment signUpFragment = new SignUpFragment();
        Bundle signUpFragmentArguments = new Bundle();
        signUpFragmentArguments.putSerializable("Firebase", firebase);
        signUpFragment.setArguments(signUpFragmentArguments);
        getFragmentManager().beginTransaction().replace(R.id.mainActivityContainer, signUpFragment, SIGNUPFRAGMENTTAG).addToBackStack(null).commit();
    }

    @Override
    public void returnToLoginFragment() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void navigateToViewMessagesFragment(User selectedContact) {

        ViewMessagesFragment viewMessagesFragment = new ViewMessagesFragment();
        Bundle viewMessagesFragmentArguments = new Bundle();
        viewMessagesFragmentArguments.putSerializable("Firebase", firebase);
        viewMessagesFragmentArguments.putSerializable("SelectedContact", selectedContact);
        viewMessagesFragmentArguments.putSerializable("CurrentlyLoggedInUser", currentlyLoggedInUser);
        viewMessagesFragment.setArguments(viewMessagesFragmentArguments);
        getFragmentManager().beginTransaction().replace(R.id.mainActivityContainer, viewMessagesFragment, VIEWMESSAGESFRAGMENTTAG).addToBackStack(null).commit();
    }

    @Override
    public void navigateToViewContactFragment(User selectedContact) {

        ViewContactFragment viewContactFragment = new ViewContactFragment();
        Bundle viewContactFragmentArguments = new Bundle();
        viewContactFragmentArguments.putSerializable("SelectedContact", selectedContact);
        viewContactFragment.setArguments(viewContactFragmentArguments);
        getFragmentManager().beginTransaction().replace(R.id.mainActivityContainer, viewContactFragment, VIEWCONTACTFRAGMENTTAG).addToBackStack(null).commit();
    }

    @Override
    public void navigateToViewMessagesFragment(Conversation conversation) {

        String otherContactEmail = "";
        if(conversation.getParticipant1Email().equals(currentlyLoggedInUser.getEmail())){
            otherContactEmail = conversation.getParticipant2Email();
        }
        else if (conversation.getParticipant2Email().equals(currentlyLoggedInUser.getEmail())){
            otherContactEmail = conversation.getParticipant1Email();
        }

        firebase.child("users").child(otherContactEmail.replaceAll("[@.]", "+")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                ViewMessagesFragment viewMessagesFragment = new ViewMessagesFragment();
                Bundle viewMessagesFragmentArguments = new Bundle();
                viewMessagesFragmentArguments.putSerializable("Firebase", firebase);
                viewMessagesFragmentArguments.putSerializable("SelectedContact", user);
                viewMessagesFragmentArguments.putSerializable("CurrentlyLoggedInUser", currentlyLoggedInUser);
                viewMessagesFragment.setArguments(viewMessagesFragmentArguments);
                getFragmentManager().beginTransaction().replace(R.id.mainActivityContainer, viewMessagesFragment, VIEWMESSAGESFRAGMENTTAG).addToBackStack(null).commit();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

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
}
