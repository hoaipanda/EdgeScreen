package com.navigation.edgescreen.activity;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.navigation.edgescreen.Common.SharePrePosFragment;
import com.navigation.edgescreen.Common.SharePreTheme;
import com.navigation.edgescreen.DepthTransformation;
import com.navigation.edgescreen.R;
import com.navigation.edgescreen.adapter.ViewPagerAdapter;
import com.navigation.edgescreen.fragment.AppFragment;
import com.navigation.edgescreen.fragment.MusicFragment;
import com.navigation.edgescreen.fragment.NotyFragment;
import com.navigation.edgescreen.fragment.PhoneBookFragment;
import com.navigation.edgescreen.fragment.PlannerFragment;
import com.navigation.edgescreen.fragment.WeatherFragment;
import com.navigation.edgescreen.service.EdgeService;

import java.util.List;
import java.util.logging.SocketHandler;

public class MainActivity extends AppCompatActivity {
    private ViewPager pager;
    private ViewPagerAdapter pagerAdapter;
    private ImageView im1, im2, im3, im4, im5, imBg;
    private RelativeLayout lySetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        startIntent = new Intent();
        checkOverlay();

        stopService(new Intent(MainActivity.this, EdgeService.class));
        setUpViewPager();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        startService(new Intent(MainActivity.this, EdgeService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        String theme = SharePreTheme.getInstance(this).getTheme();
        if (theme.length() > 0) {
            Glide.with(this).load(Uri.parse("file:///android_asset/theme/" + theme)).into(imBg);
        }
        if (SharePrePosFragment.getInstance(this).getPosFragment(1 + "").length() != 0) {
            setUpViewPager();
        }
    }

    private void initView() {
        imBg = findViewById(R.id.imBg);
        lySetting = findViewById(R.id.lySetting);
        pager = findViewById(R.id.pager);
        im1 = findViewById(R.id.im1);
        im2 = findViewById(R.id.im2);
        im3 = findViewById(R.id.im3);
        im4 = findViewById(R.id.im4);
        im5 = findViewById(R.id.im5);

        lySetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });
    }

    private void setUpPosition(int i) {
        switch (i) {
            case 1:
                im1.setColorFilter(Color.WHITE);
                im2.setColorFilter(getResources().getColor(R.color.gray));
                im3.setColorFilter(getResources().getColor(R.color.gray));
                im4.setColorFilter(getResources().getColor(R.color.gray));
                im5.setColorFilter(getResources().getColor(R.color.gray));
                break;
            case 2:
                im2.setColorFilter(Color.WHITE);
                im1.setColorFilter(getResources().getColor(R.color.gray));
                im3.setColorFilter(getResources().getColor(R.color.gray));
                im4.setColorFilter(getResources().getColor(R.color.gray));
                im5.setColorFilter(getResources().getColor(R.color.gray));
                break;
            case 3:
                im3.setColorFilter(Color.WHITE);
                im2.setColorFilter(getResources().getColor(R.color.gray));
                im1.setColorFilter(getResources().getColor(R.color.gray));
                im4.setColorFilter(getResources().getColor(R.color.gray));
                im5.setColorFilter(getResources().getColor(R.color.gray));
                break;
            case 4:
                im4.setColorFilter(Color.WHITE);
                im2.setColorFilter(getResources().getColor(R.color.gray));
                im3.setColorFilter(getResources().getColor(R.color.gray));
                im1.setColorFilter(getResources().getColor(R.color.gray));
                im5.setColorFilter(getResources().getColor(R.color.gray));
                break;
            case 5:
                im5.setColorFilter(Color.WHITE);
                im2.setColorFilter(getResources().getColor(R.color.gray));
                im3.setColorFilter(getResources().getColor(R.color.gray));
                im4.setColorFilter(getResources().getColor(R.color.gray));
                im1.setColorFilter(getResources().getColor(R.color.gray));
                break;
        }
    }


    private void setUpViewPager() {
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        DepthTransformation depthTransformation = new DepthTransformation();
        pager.setPageTransformer(true, depthTransformation);
        AppFragment appFragment = new AppFragment();
        MusicFragment musicFragment = new MusicFragment();
        NotyFragment notyFragment = new NotyFragment();
        PhoneBookFragment phoneBookFragment = new PhoneBookFragment();
        PlannerFragment plannerFragment = new PlannerFragment();
        WeatherFragment weatherFragment = new WeatherFragment();
        if (SharePrePosFragment.getInstance(this).getPosFragment(1 + "").length() == 0) {
            pagerAdapter.addFragment(appFragment);
            pagerAdapter.addFragment(phoneBookFragment);
            pagerAdapter.addFragment(weatherFragment);
//            pagerAdapter.addFragment(musicFragment);
            pagerAdapter.addFragment(notyFragment);
            pagerAdapter.addFragment(plannerFragment);
        } else {
            for (int i = 0; i < 5; i++) {
                String frg = SharePrePosFragment.getInstance(this).getPosFragment(i + "");
                switch (frg) {
                    case "app":
                        pagerAdapter.addFragment(appFragment);
                        break;
                    case "phone":
                        pagerAdapter.addFragment(phoneBookFragment);
                        break;
                    case "weather":
                        pagerAdapter.addFragment(weatherFragment);
                        break;
                    case "noty":
                        pagerAdapter.addFragment(notyFragment);
                        break;
                    case "planner":
                        pagerAdapter.addFragment(plannerFragment);
                        break;
                }
            }
        }


        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(6);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()

        {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                setUpPosition(i + 1);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    private void checkOverlay() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(MainActivity.this)) {
                new AlertDialog.Builder(this)
                        .setTitle("Notification")
                        .setCancelable(false)
                        .setMessage("Application need manager overlay permission, you can turn on this permission ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                        Uri.parse("package:" + getPackageName()));
                                startActivityForResult(intent, 2);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "Permission Obligatory", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        }
    }

    public boolean checkService(String str) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = manager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo info : services) {
            if (info.service.getClassName().toUpperCase().equals(str.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    private ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2 && !Settings.canDrawOverlays(this)) {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.show();
            new CountDownTimer(15000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (Settings.canDrawOverlays(MainActivity.this)) {
                        this.cancel(); // cancel the timer
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFinish() {
                    progressDialog.dismiss();
                    if (!Settings.canDrawOverlays(MainActivity.this)) {
                        checkOverlay();
                    } else {
                        startService(new Intent(MainActivity.this, EdgeService.class));
                    }
                }
            }.start();
        } else {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                Log.e("hoaiii", requestCode + "");
                fragment.onActivityResult(requestCode, resultCode, data);
            }

        }


    }


    private Intent startIntent;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                finish();
                return;
            }
        }
        finish();
        startActivity(startIntent);
    }

}
