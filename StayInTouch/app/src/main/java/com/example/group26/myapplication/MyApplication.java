package com.example.group26.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.firebase.client.Firebase;

import java.io.ByteArrayOutputStream;

/**
 * Created by Carlos on 4/16/2016.
 */
public class MyApplication extends android.app.Application {

    Firebase ref;
    String dummyProfilePic;

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        ref = new Firebase(Constants.FIREBASEURL);
    }

    public Firebase getFireBase(){
        return ref;
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
}
