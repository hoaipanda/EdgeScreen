package com.navigation.edgescreen.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.navigation.edgescreen.Common.SharePrePosFragment;
import com.navigation.edgescreen.R;
import com.navigation.edgescreen.SimpleItemTouchHelperCallback;
import com.navigation.edgescreen.adapter.ScreenAdapter;
import com.navigation.edgescreen.data.Screen;

import java.util.ArrayList;

public class ScreenActivity extends AppCompatActivity {
    private ArrayList<Screen> listScreen = new ArrayList<>();
    private RecyclerView rvScreen;
    private ScreenAdapter screenAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        rvScreen = findViewById(R.id.rvScreen);
        if (SharePrePosFragment.getInstance(this).getPosFragment(1 + "").length() > 0) {
            for (int i = 0; i < 6; i++) {
                String name = SharePrePosFragment.getInstance(this).getPosFragment((i) + "");
                listScreen.add(new Screen(name, name + ".png"));
            }
        } else {
            listScreen.add(new Screen("app", "app.png"));
            listScreen.add(new Screen("phone", "phone.png"));
            listScreen.add(new Screen("weather", "weather.png"));
            listScreen.add(new Screen("noty", "noty.png"));
            listScreen.add(new Screen("planner", "planner.png"));
        }
        screenAdapter = new ScreenAdapter(this, listScreen);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvScreen.setLayoutManager(linearLayoutManager);
        rvScreen.setAdapter(screenAdapter);
        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(screenAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rvScreen);


    }

    @Override
    protected void onStop() {
        super.onStop();
        for (int i = 0; i < 5; i++) {
            SharePrePosFragment.getInstance(this).addPosFragment(i + "", listScreen.get(i).getName());
        }
    }
}
