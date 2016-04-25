package com.example.group26.stayintouchwithfragments;

import com.firebase.client.Firebase;

import java.io.Serializable;

/**
 * Created by carlosrosario on 4/23/16.
 */
public class FirebaseSerialize extends Firebase implements Serializable {
    public FirebaseSerialize(String url) {
        super(url);
    }
}
