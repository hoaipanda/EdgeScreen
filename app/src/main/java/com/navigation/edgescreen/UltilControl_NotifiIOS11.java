package com.navigation.edgescreen;

import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.hardware.Camera;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class UltilControl_NotifiIOS11 {
    private Context mContext;
    private WifiManager wifiManager;
    private BluetoothAdapter mBluetoothAdapter;
    NotificationManager mNotificationManager;
    boolean isEnabledairPlane;
    AudioManager mAudioManager = null;
    private static Camera camera;

    public UltilControl_NotifiIOS11(Context mContext) {
        this.mContext = mContext;
        wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        isEnabledairPlane = Settings.System.getInt(mContext.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) == 1;
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

    }

    public void toggleWiFi(Context context, boolean status) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (status && !wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        } else if (!status && wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }
    public boolean isBluetooth(){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter.isEnabled();
    }
    public boolean setBluetooth(boolean enable) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabled = bluetoothAdapter.isEnabled();
        if (enable && !isEnabled) {
            return bluetoothAdapter.enable();
        } else if (!enable && isEnabled) {
            return bluetoothAdapter.disable();
        }
        return true;
    }
    public void setAutoOrientationEnabled(Context context, boolean enabled) {
        Settings.System.putInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, enabled ? 1 : 0);
    }
    public void setSilentEnable(Context context, boolean enabled) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
        if (enabled) {
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        } else {
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
    }
    public boolean isWifiEnble(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }
    public boolean isSilentEnable(Context context) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
        int ringermode = mAudioManager.getRingerMode();
        if (ringermode == mAudioManager.RINGER_MODE_SILENT) {
            return true;
        }
        return false;
    }
    public boolean isAutoOrientaitonEnable(Context context) {
        if (Settings.System.getInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
            return true;
        }
        return false;
    }
    private Boolean checBlueTooth() {
        try {
            return mBluetoothAdapter.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
    public void setBRIGHTNESS(int value) {
        Settings.System.putInt(mContext.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, value);
    }
    public static void stopCamera() {
        try {
            if (camera != null) {
                camera.release();
                camera = null;
            }
        } catch (Exception e) {

        }
    }

    //setAutoOrientationEnabled
    private Boolean setAutoOrientationEnabled() {
        return Settings.System.getInt(mContext.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1;
    }

    private void OnROTATION() {
        Settings.System.putInt(mContext.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 1);
    }

    private void OffROTATION() {
        Settings.System.putInt(mContext.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
    }

    public void ScreenSettingOrtation(CheckBox inbtnOrtation) {
        try {
            if (!setAutoOrientationEnabled()) {
                OnROTATION();
                inbtnOrtation.setButtonDrawable(R.drawable.orientation_on_notificationios11);
            } else {
                OffROTATION();
                inbtnOrtation.setButtonDrawable(R.drawable.orientation_off_notificationios11);
            }
        } catch (Exception e) {
            Toast.makeText(mContext, "Try failed!", Toast.LENGTH_SHORT).show();
        }
    }
    public void addAudio(ImageButton ibtnAudio) {
        switch (mAudioManager.getRingerMode()) {
            case AudioManager.RINGER_MODE_NORMAL:
                ibtnAudio.setImageResource(R.drawable.nightmode_off_notificationios11);
                break;
            case AudioManager.RINGER_MODE_SILENT:
                ibtnAudio.setImageResource(R.drawable.nightmode_on_notificationios11);
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                ibtnAudio.setImageResource(R.drawable.nightmode_off_notificationios11);
                break;
        }
    }

    public void checkvibration(CheckBox imgb) {

        switch (mAudioManager.getRingerMode()) {
            case AudioManager.RINGER_MODE_NORMAL:
                imgb.setButtonDrawable(R.drawable.nightmode_on_notificationios11);
                mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                break;
            case AudioManager.RINGER_MODE_SILENT:
                imgb.setButtonDrawable(R.drawable.nightmode_off_notificationios11);
                mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                imgb.setButtonDrawable(R.drawable.nightmode_on_notificationios11);
                mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                break;
        }
    }

    public ArrayList<String> getImageFromAssert(Context context, String nameFolder) {
        ArrayList<String> listStrImage = new ArrayList<>();
        AssetManager assetManager = context.getAssets();
        String[] files = new String[0];
        try {
            files = assetManager.list(nameFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < Arrays.asList(files).size(); i++) {
            listStrImage.add(nameFolder + "/" + Arrays.asList(files).get(i));
        }
        return listStrImage;
    }

    public int getLight (Context context){
        try {
            return Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return 10;
        }
    }
    public void saveStringPre (String value, String name, SharedPreferences pre){
        SharedPreferences.Editor editor = pre.edit();
        editor.putString(name, value);
        editor.apply();
    }
    public void saveIntPre (int value, String name, SharedPreferences pre){
        SharedPreferences.Editor editor = pre.edit();
        editor.putInt(name, value);
        editor.apply();
    }
    public void savePreBoolean (boolean value, String name, SharedPreferences pre){
        SharedPreferences.Editor editor = pre.edit();
        editor.putBoolean(name, value);
        editor.apply();
    }

    public void runVibrateShort (Context context){
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(200L);
    }
    public int getOffsetY(View view) {
        int mOffset[] = new int[2];
        view.getLocationOnScreen(mOffset);
        return mOffset[1];
    }
}