package com.navigation.edgescreen.data;

import android.support.annotation.NonNull;

import java.io.Serializable;


public class Contact implements Comparable<Contact>, Serializable {
    public int id;
    public String name;
    public String mobileNumber;
    public String photoString;
    public String nick;
    public String address;
    public String email;

    public Contact() {
    }

    public Contact(int id, String name, String mobileNumber, String photoString, String email) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.photoString = photoString;
        this.id = id;
        this.email = email;
    }

    public Contact(int id, String name, String mobileNumber, String photoString, int isLike, String nick, String address, String email) {
        this.id = id;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.photoString = photoString;
        this.nick = nick;
        this.address = address;
        this.email = email;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPhotoString() {
        return photoString;
    }

    public void setPhotoString(String photoString) {
        this.photoString = photoString;
    }

    @Override
    public int compareTo(@NonNull Contact o) {
        return name.compareTo(o.getName());
    }


}

