package com.navigation.edgescreen.Common;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreTheme {
    private static SharePreTheme instance;
    public Context context;

    private SharePreTheme(Context context) {
        this.context = context;
    }

    public static SharePreTheme getInstance(Context context) {
        if (instance == null) {
            instance = new SharePreTheme(context);
        }
        return instance;
    }

    public void addThem(String values) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Contains.DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Contains.THEME, values);
        editor.apply();
    }

    public String getTheme() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Contains.DATA, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Contains.THEME, "");
    }

}



