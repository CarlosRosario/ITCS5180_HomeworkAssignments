package com.example.group26.myapplication;

import com.firebase.client.Firebase;

/**
 * Created by Carlos on 4/16/2016.
 */
public class MyApplication extends android.app.Application {

    Firebase ref;

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        ref = new Firebase(Constants.FIREBASEURL);
    }

    public Firebase getFireBase(){
        return ref;
    }
}
