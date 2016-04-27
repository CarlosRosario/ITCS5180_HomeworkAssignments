package com.example.group26.stayintouchwithfragments;

import java.io.Serializable;

/**
 * Created by carlosrosario on 4/23/16.
 */
public class User implements Serializable{

    private String email;
    private String fullName;
    private String password;
    private String phoneNumber;
    private String base64Picture;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBase64Picture() {
        return base64Picture;
    }

    public void setBase64Picture(String base64Picture) {
        this.base64Picture = base64Picture;
    }
}
