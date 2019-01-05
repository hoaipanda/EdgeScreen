package com.navigation.edgescreen.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.navigation.edgescreen.Common.AppUtils;
import com.navigation.edgescreen.Common.SharePreTheme;
import com.navigation.edgescreen.R;
import com.navigation.edgescreen.adapter.ThemeAdapter;

import java.util.ArrayList;

public class ThemeActivity extends AppCompatActivity {
    private ArrayList<String> listTheme = new ArrayList<>();
    private RecyclerView rvTheme;
    private ThemeAdapter themeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        rvTheme = findViewById(R.id.rvTheme);
        listTheme = AppUtils.getListTheme(this);
        themeAdapter = new ThemeAdapter(this, listTheme);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rvTheme.setLayoutManager(gridLayoutManager);
        rvTheme.setAdapter(themeAdapter);
        themeAdapter.setOnClickItemTheme(new ThemeAdapter.OnClickItemTheme() {
            @Override
            public void onClickTheme(String theme) {
                SharePreTheme.getInstance(ThemeActivity.this).addThem(theme);
                finish();
            }
        });
    }
}
