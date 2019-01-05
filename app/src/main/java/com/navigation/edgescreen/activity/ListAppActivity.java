package com.navigation.edgescreen.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.navigation.edgescreen.Common.AppUtils;
import com.navigation.edgescreen.Common.Contains;
import com.navigation.edgescreen.R;
import com.navigation.edgescreen.adapter.AppListAdapter;
import com.navigation.edgescreen.data.AppInfo;

import java.util.ArrayList;

public class ListAppActivity extends AppCompatActivity {
    private AppListAdapter appListAdapter;
    private RecyclerView rvApp;
    private ArrayList<AppInfo> listApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_app);
        rvApp = findViewById(R.id.rvApp);
        listApp = AppUtils.getListAppInfo(this);
        appListAdapter = new AppListAdapter(listApp);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        rvApp.setLayoutManager(gridLayoutManager);
        rvApp.setAdapter(appListAdapter);
        appListAdapter.setOnClickItemAppList(new AppListAdapter.OnClickItemAppList() {
            @Override
            public void onClickApp(AppInfo appInfo) {
                Intent intent = new Intent();
                intent.putExtra(Contains.APPINFO, appInfo.getPack());
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }
}
