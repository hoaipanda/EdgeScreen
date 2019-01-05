package com.navigation.edgescreen.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.navigation.edgescreen.Common.AppUtils;
import com.navigation.edgescreen.Common.SharePreContact;
import com.navigation.edgescreen.Common.SharePreIcon;
import com.navigation.edgescreen.R;
import com.navigation.edgescreen.service.EdgeService;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {
    private ImageView imCheck;
    private TextView tvContact;
    private RelativeLayout lyPhone, lyTheme, lyScreen, lyIcon, lyPolicy;
    private boolean isCheck = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initListen();
        isCheck = SharePreIcon.getInstance(SettingActivity.this).getIcon();
        if (isCheck) {
            imCheck.setImageResource(R.drawable.on);
        } else {
            imCheck.setImageResource(R.drawable.off);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int demm = 0;
        for (int i = 1; i < 7; i++) {
            if (AppUtils.getContacts(SettingActivity.this, SharePreContact.getInstance(this).getContact(i + "")) != null) {
                demm++;
            }
        }
        if (demm > 1) {
            tvContact.setText(demm + " contacts");
        } else {
            tvContact.setText(demm + " contact");
        }

    }

    private void initListen() {
        imCheck.setOnClickListener(lsClick);
        lyPolicy.setOnClickListener(lsClick);
        lyTheme.setOnClickListener(lsClick);
        lyIcon.setOnClickListener(lsClick);
        lyScreen.setOnClickListener(lsClick);
        lyPhone.setOnClickListener(lsClick);
    }

    private View.OnClickListener lsClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imCheck:
                    isCheck = !isCheck;
                    if (isCheck) {
                        imCheck.setImageResource(R.drawable.on);
                        SharePreIcon.getInstance(SettingActivity.this).addIcon(true);
                    } else {
                        imCheck.setImageResource(R.drawable.off);
                        SharePreIcon.getInstance(SettingActivity.this).addIcon(false);
                    }
                    break;
                case R.id.lyPhone:
                    startActivity(new Intent(SettingActivity.this, PhoneBookActivity.class));
                    break;
                case R.id.lyTheme:
                    startActivity(new Intent(SettingActivity.this, ThemeActivity.class));
                    break;
                case R.id.lyScreen:
                    startActivity(new Intent(SettingActivity.this, ScreenActivity.class));
                    break;
                case R.id.lyIcon:
                    startActivity(new Intent(SettingActivity.this, IconActivity.class));
                    break;
                case R.id.lyPolicy:
                    break;
            }
        }
    };

    private void initView() {
        imCheck = findViewById(R.id.imCheck);
        tvContact = findViewById(R.id.tvNumContact);
        lyPhone = findViewById(R.id.lyPhone);
        lyScreen = findViewById(R.id.lyScreen);
        lyIcon = findViewById(R.id.lyIcon);
        lyPolicy = findViewById(R.id.lyPolicy);
        lyTheme = findViewById(R.id.lyTheme);
    }
}
