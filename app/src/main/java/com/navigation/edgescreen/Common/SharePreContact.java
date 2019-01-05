package com.navigation.edgescreen.Common;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreContact {
    private static SharePreContact instance;
    public Context context;

    private SharePreContact(Context context) {
        this.context = context;
    }

    public static SharePreContact getInstance(Context context) {
        if (instance == null) {
            instance = new SharePreContact(context);
        }
        return instance;
    }

    public void addContact(String key, int values) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Contains.DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, values);
        editor.apply();
    }

    public int getContact(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Contains.DATA, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, -1);
    }

    public void removeContact(String key) {
        SharedPreferences mySPrefs = context.getSharedPreferences(Contains.DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySPrefs.edit();
        editor.remove(key);
        editor.apply();
    }
}



