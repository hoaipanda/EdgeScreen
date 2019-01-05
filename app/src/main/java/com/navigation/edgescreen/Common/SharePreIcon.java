package com.navigation.edgescreen.Common;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreIcon {
    private static SharePreIcon instance;
    public Context context;

    private SharePreIcon(Context context) {
        this.context = context;
    }

    public static SharePreIcon getInstance(Context context) {
        if (instance == null) {
            instance = new SharePreIcon(context);
        }
        return instance;
    }

    public void addIcon(boolean values) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Contains.DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Contains.ICON, values);
        editor.apply();
    }

    public boolean getIcon() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Contains.DATA, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Contains.ICON, true);
    }

}



