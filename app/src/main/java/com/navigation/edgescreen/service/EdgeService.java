package com.navigation.edgescreen.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.navigation.edgescreen.Common.Contains;
import com.navigation.edgescreen.Common.SharePreIcon;
import com.navigation.edgescreen.R;
import com.navigation.edgescreen.activity.MainActivity;
import com.navigation.edgescreen.widget.MyGroupView;


public class EdgeService extends Service implements View.OnTouchListener {
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParamIcon;
    private int DOWN_X, DOWN_Y, MOVE_X, MOVE_Y, xparam, yparam;
    private MyGroupView mViewIcon;
    private View viewIcon;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        if (SharePreIcon.getInstance(this).getIcon()) {
            createViewIcon();
            showViewIcon();
        }

        return START_STICKY;
    }

    private void createViewIcon() {
        mViewIcon = new MyGroupView(this);
        viewIcon = View.inflate(this, R.layout.screen_icon, mViewIcon);
        ImageView imIcon = viewIcon.findViewById(R.id.imIcon);
        SharedPreferences preferences = getSharedPreferences(Contains.DATA, Context.MODE_PRIVATE);
        String icon = preferences.getString("stringicon", "");
        if (icon.length() > 0) {
            Glide.with(EdgeService.this).load(Uri.parse("file:///android_asset/icon/" + icon)).into(imIcon);
        } else {
            Glide.with(EdgeService.this).load(Uri.parse("file:///android_asset/icon/" + "icon2")).into(imIcon);
        }

        mViewIcon.setOnTouchListener(this);

        mParamIcon = new WindowManager.LayoutParams();
        mParamIcon.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParamIcon.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParamIcon.gravity = Gravity.LEFT;
        mParamIcon.format = PixelFormat.TRANSLUCENT;//trong suot
        mParamIcon.type = WindowManager.LayoutParams.TYPE_PHONE;// noi tren all be mat
        mParamIcon.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        viewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dialogIntent = new Intent(EdgeService.this, MainActivity.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dialogIntent);

                try {
                    mWindowManager.removeView(mViewIcon);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showViewIcon() {
        try {
            if (mViewIcon != null) {
                mWindowManager.removeView(mViewIcon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mWindowManager.addView(mViewIcon, mParamIcon);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:

                xparam = mParamIcon.x;
                yparam = mParamIcon.y;

                DOWN_X = (int) motionEvent.getRawX();
                DOWN_Y = (int) motionEvent.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                MOVE_X = (int) motionEvent.getRawX() - DOWN_X;
                MOVE_Y = (int) motionEvent.getRawY() - DOWN_Y;
                updateViewIcon(MOVE_X, MOVE_Y);
                break;
        }
        return false;
    }

    private void updateViewIcon(int x, int y) {
        mParamIcon.x = x + xparam;
        mParamIcon.y = y + yparam;
        mWindowManager.updateViewLayout(mViewIcon, mParamIcon);
    }
}
