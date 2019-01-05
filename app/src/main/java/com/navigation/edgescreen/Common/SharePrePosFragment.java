package com.navigation.edgescreen.Common;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePrePosFragment {
    private static SharePrePosFragment instance;
    public Context context;

    private SharePrePosFragment(Context context) {
        this.context = context;
    }

    public static SharePrePosFragment getInstance(Context context) {
        if (instance == null) {
            instance = new SharePrePosFragment(context);
        }
        return instance;
    }

    public void addPosFragment(String key, String values) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Contains.DATAPOSFRAGMENT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, values);
        editor.apply();
    }

    public String getPosFragment(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Contains.DATAPOSFRAGMENT, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

}



