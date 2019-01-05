package com.navigation.edgescreen.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.navigation.edgescreen.Common.AppUtils;
import com.navigation.edgescreen.Common.Contains;
import com.navigation.edgescreen.R;
import com.navigation.edgescreen.adapter.IconAdapter;
import com.navigation.edgescreen.data.Contact;

import java.util.ArrayList;

public class IconActivity extends AppCompatActivity {
    private ArrayList<String> listIcon = new ArrayList<>();
    private RecyclerView rvIcon;
    private IconAdapter iconAdapter;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon);
        preferences = getSharedPreferences(Contains.DATA, Context.MODE_PRIVATE);
        rvIcon = findViewById(R.id.rvIcon);
        listIcon = AppUtils.getListIcon(this);
        iconAdapter = new IconAdapter(this, listIcon);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rvIcon.setLayoutManager(gridLayoutManager);
        rvIcon.setAdapter(iconAdapter);
        iconAdapter.setOnClickItemTheme(new IconAdapter.OnClickItemTheme() {
            @Override
            public void onClickTheme(String theme) {
                preferences.edit().putString("stringicon", theme).commit();
                finish();
            }
        });
    }
}
