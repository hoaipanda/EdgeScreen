package com.navigation.edgescreen.fragment;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.AlarmClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.navigation.edgescreen.Common.Contains;
import com.navigation.edgescreen.MyViewBG;
import com.navigation.edgescreen.adapter.AdapterAddSong;
import com.navigation.edgescreen.asyntask.AsyncTackReadMusic;
import com.navigation.edgescreen.asyntask.GetMusicComplete;
import com.navigation.edgescreen.asyntask.MusicController;
import com.navigation.edgescreen.service.MusicControlService;
import com.navigation.edgescreen.service.NightService;
import com.navigation.edgescreen.R;
import com.navigation.edgescreen.UltilControl_NotifiIOS11;
import com.navigation.edgescreen.data.RowListSong;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.AUDIO_SERVICE;
import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotyFragment extends Fragment implements MediaController.MediaPlayerControl, View.OnLongClickListener {
    private static final String SETTINGS_PACKAGE = "com.android.settings";
    private static final String SETTINGS_CLASS_DATA_USAGE_SETTINGS = "com.android.settings.Settings$DataUsageSummaryActivity";

    private Activity mContext;
    private View v;
    private ImageView cbPlane, cb3G, cbWifi, cbBluetooth;
    private CheckBox cbSlient;
    private TextView tvNameSong, tvNameArtist;
    private ImageView imgBackSong, imgNextSong, imgFlash, tvCancelMusic, imgNightMode, imgOrientation, imgStartPause, imgSlide;
    private LinearLayout autoBrightness, lnlFlash, lnlClock, lnlCaculator, lnlCamera, lnlControl, lnlOrientation, lnlNightMode;
    private RelativeLayout rltParentControl, rltSongs, rltMusic;
    private int widthtScreen = 0, heightScreen = 0;
    private SharedPreferences sharedPreferences;
    private UltilControl_NotifiIOS11 methodUltils;
    private Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    private boolean isCamera;
    static Camera.Parameters paramsCamera;
    private boolean isCheckWifi, isCheckBluetooth;
    private ArrayList<RowListSong> arrSongChoose;
    private AdapterAddSong adapterAddSong;
    public static MusicControlService musicSrv;
    private boolean paused = false, playbackPaused = false;
    private ListView lv_add_song;
    private int poSong = 0;
    private Intent playIntent;
    private MusicController controller;
    private MyViewBG imgLight, imgSound;

    public NotyFragment() {
        // Required empty public constructor
    }

    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicControlService.MusicBinder binder = (MusicControlService.MusicBinder) service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(arrSongChoose);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    private void setController() {
        controller = new MusicController(mContext);
        //set previous and next button listeners
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });
        //set and show
        controller.setMediaPlayer(this);
        controller.setAnchorView(v.findViewById(R.id.lv_add_song));
        controller.setEnabled(true);
    }

    private void playNext() {
        try {
            musicSrv.playNext();
            if (playbackPaused) {
                imgStartPause.setImageResource(R.drawable.pause_music);
                setController();
                playbackPaused = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playPrev() {
        try {
            musicSrv.playPrev();
            if (playbackPaused) {
                setController();
                imgStartPause.setImageResource(R.drawable.pause_music);
                playbackPaused = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_noty, container, false);
        mContext = getActivity();
        methodUltils = new UltilControl_NotifiIOS11(mContext);
        sharedPreferences = mContext.getSharedPreferences(Contains.SAVE, Context.MODE_PRIVATE);
        initView();
        iniListen();
        playIntent = new Intent(mContext, MusicControlService.class);
        mContext.bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
        mContext.startService(playIntent);
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        localIntentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        localIntentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        mContext.registerReceiver(this.StrengthWifi, new IntentFilter(localIntentFilter));
        mContext.registerReceiver(this.ActionAirPlane, new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED));
        mContext.registerReceiver(this.mReceiverBlutooth, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unbindService(musicConnection);
        mContext.unregisterReceiver(mReceiverBlutooth);
        mContext.unregisterReceiver(StrengthWifi);
        mContext.unregisterReceiver(ActionAirPlane);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onResume() {
        super.onResume();
        setupData();
    }

    public void setSong(String nameSong, String nameArtist, int poSong) {
        Log.e("hoaiii", nameSong);
        tvNameArtist.setText(nameArtist);
        tvNameSong.setText(nameSong);
        adapterAddSong.updateSongChoose(poSong);
    }

    public void controlStartPause() {
        if (isPlaying()) {
            pause();
            imgStartPause.setImageResource(R.drawable.play_music);
        } else {
            start();
            imgStartPause.setImageResource(R.drawable.pause_music);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setupData() {
        setNetWorkState();
        if (methodUltils.isBluetooth()) {
            cbBluetooth.setImageResource(R.drawable.bluetooth_on);
            isCheckBluetooth = true;
        } else {
            isCheckBluetooth = false;
            cbBluetooth.setImageResource(R.drawable.bluetooth_off);
        }

        if (methodUltils.isSilentEnable(mContext)) {
            cbSlient.setChecked(true);
        } else cbSlient.setChecked(false);
        if (sharedPreferences.getBoolean(Contains.NIGHT_MODE, false)) {
            imgNightMode.setImageResource(R.drawable.nigtmode_on);
        } else imgNightMode.setImageResource(R.drawable.nightmode_off);
        if (methodUltils.isAutoOrientaitonEnable(mContext)) {
            imgOrientation.setImageResource(R.drawable.lock_on);
        } else imgOrientation.setImageResource(R.drawable.lock_off);
        if (Settings.Global.getInt(mContext.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) != 0) {
            cbPlane.setImageResource(R.drawable.airplane_on);
        } else {
            cbPlane.setImageResource(R.drawable.airplane_off);
        }

        arrSongChoose = new ArrayList<>();
        AsyncTackReadMusic asyncTackReadMusic = new AsyncTackReadMusic(mContext, new GetMusicComplete() {
            @Override
            public void complete(ArrayList<RowListSong> arrSongs) {
                try {
                    arrSongChoose.addAll(arrSongs);
                    adapterAddSong = new AdapterAddSong(mContext, arrSongChoose, -1);
                    lv_add_song.setAdapter(adapterAddSong);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        asyncTackReadMusic.execute();

        lv_add_song.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    poSong = position;
                    musicSrv.setSong(poSong);
                    musicSrv.playSong();

                    tvNameArtist.setText(arrSongChoose.get(poSong).getArtist());
                    tvNameSong.setText(arrSongChoose.get(poSong).getTitle());

                    adapterAddSong.updateSongChoose(position);
                    imgStartPause.setImageResource(R.drawable.pause_music);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        imgLight.setBmImage(BitmapFactory.decodeResource(getResources(), R.drawable.light2), BitmapFactory.decodeResource(getResources(), R.drawable.light));
        imgSound.setBmImage(BitmapFactory.decodeResource(getResources(), R.drawable.sound2), BitmapFactory.decodeResource(getResources(), R.drawable.sound));
        AudioManager am = (AudioManager) mContext.getSystemService(AUDIO_SERVICE);
        imgSound.setProgress(am.getStreamVolume(AudioManager.STREAM_MUSIC));

        imgLight.setProgress((methodUltils.getLight(mContext) * 100) / 255);

        imgLight.setOnTouchViewSeekBar(new MyViewBG.OnTouchViewSeekBar() {
            @Override
            public void touchDown(MyViewBG v) {

//                slu_layout_unlock.setTouchEnabled(false);
            }

            @Override
            public void touchMove(MyViewBG v, int value) {
                if (value > 4) {
                    methodUltils.setBRIGHTNESS((int) (value * 2.55f));
                }
            }

            @Override
            public void touchUp(MyViewBG v) {
//                slu_layout_unlock.setTouchEnabled(true);
            }
        });
        final AudioManager audioManager = (AudioManager) mContext.getSystemService(AUDIO_SERVICE);
        imgSound.setOnTouchViewSeekBar(new MyViewBG.OnTouchViewSeekBar() {
            @Override
            public void touchDown(MyViewBG v) {

//                slu_layout_unlock.setTouchEnabled(false);
            }

            @Override
            public void touchMove(MyViewBG v, int value) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, value, 0);
            }

            @Override
            public void touchUp(MyViewBG v) {

//                slu_layout_unlock.setTouchEnabled(true);
            }
        });

    }

    private void setNetWorkState() {
        WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (wifiManager.isWifiEnabled()) {
            cbWifi.setImageResource(R.drawable.wifi_on);
            isCheckWifi = true;
        } else {
            isCheckBluetooth = false;
            cbWifi.setImageResource(R.drawable.wifi_off);
        }
        if (networkInfo != null) {
            if (networkInfo.isAvailable() && !wifiManager.isWifiEnabled()) {
                cbWifi.setImageResource(R.drawable.wifi_off);
                cb3G.setImageResource(R.drawable.anten_on);
                isCheckWifi = false;
            } else {
                cb3G.setImageResource(R.drawable.anten_off);
            }
        } else {
            cb3G.setImageResource(R.drawable.anten_off);
        }
    }

    public Boolean checkFlashship(Context getContext) {
        return hasFlash = mContext.getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                paramsCamera = camera.getParameters();
                isCamera = true;
            } catch (RuntimeException e) {
                isCamera = false;
                Toast.makeText(mContext, "no camera!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    public void startFlashLight(boolean show) {
        try {
            if (camera == null) {
                camera = Camera.open();
            }
            Camera.Parameters p = camera.getParameters();
            boolean on = show;
            if (on) {
                Log.i("info", "torch is turn on!");
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(p);
                SurfaceTexture mPreviewTexture = new SurfaceTexture(0);
                try {
                    camera.setPreviewTexture(mPreviewTexture);
                } catch (IOException ex) {
                    // Ignore
                }
                camera.startPreview();
            } else {
                Log.i("info", "torch is turn off!");
                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(p);
                camera.stopPreview();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }


    private void initView() {
        imgLight = v.findViewById(R.id.imgLight);
        imgSound = v.findViewById(R.id.imgSound);
        lnlControl = v.findViewById(R.id.lnlControl);
        lv_add_song = (ListView) v.findViewById(R.id.lv_add_song);
        cbPlane = v.findViewById(R.id.cbPlane);
        cbSlient = v.findViewById(R.id.cbSlient);
        cbWifi = v.findViewById(R.id.cbWifi);
        cb3G = v.findViewById(R.id.cb3G);
        imgNightMode = v.findViewById(R.id.imgNightMode);
        imgOrientation = v.findViewById(R.id.imgOrientation);
        imgBackSong = v.findViewById(R.id.imgBackSong);
        tvCancelMusic = v.findViewById(R.id.tvCancelMusic);
        imgStartPause = v.findViewById(R.id.imgStartPause);
        imgNextSong = v.findViewById(R.id.imgNextSong);
        imgFlash = v.findViewById(R.id.imgFlash);
        lnlFlash = v.findViewById(R.id.lnlFlash);
        lnlCamera = v.findViewById(R.id.lnlCamera);
        lnlCaculator = v.findViewById(R.id.lnlCaculator);
        lnlClock = v.findViewById(R.id.lnlClock);
        lnlOrientation = v.findViewById(R.id.lnlOrientation);
        autoBrightness = v.findViewById(R.id.autoBrightness);
        lnlNightMode = v.findViewById(R.id.lnlNightMode);

        cbBluetooth = v.findViewById(R.id.cbBluetooth);
        rltSongs = v.findViewById(R.id.rltSongs);
        rltMusic = v.findViewById(R.id.rltMusic);
        tvNameSong = v.findViewById(R.id.tvNameSong);
        tvNameArtist = v.findViewById(R.id.tvNameArtist);
    }

    private void iniListen() {
        cb3G.setOnClickListener(lsClick);
        cbWifi.setOnClickListener(lsClick);
        lnlFlash.setOnClickListener(lsClick);
        lnlCaculator.setOnClickListener(lsClick);
        lnlCamera.setOnClickListener(lsClick);
        lnlOrientation.setOnClickListener(lsClick);
        lnlClock.setOnClickListener(lsClick);
        cbPlane.setOnClickListener(lsClick);
        autoBrightness.setOnClickListener(lsClick);
        lnlNightMode.setOnClickListener(lsClick);
        cbBluetooth.setOnClickListener(lsClick);
        rltSongs.setOnLongClickListener(this);
        imgBackSong.setOnClickListener(lsClick);
        imgStartPause.setOnClickListener(lsClick);
        imgNextSong.setOnClickListener(lsClick);
        tvCancelMusic.setOnClickListener(lsClick);

    }

    private void go3G() {
        try {
            final Intent intent = new Intent(Intent.ACTION_MAIN, null);
            final ComponentName cn = new ComponentName(SETTINGS_PACKAGE, SETTINGS_CLASS_DATA_USAGE_SETTINGS);
            intent.setComponent(cn);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            sendMessage();
        } catch (ActivityNotFoundException e) {
            Log.v("thunt", "Data settings usage Activity is not present");
        }
    }

    private View.OnClickListener lsClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.lnlNightMode:
                    if (sharedPreferences.getBoolean(Contains.NIGHT_MODE, false)) {
                        mContext.stopService(new Intent(mContext, NightService.class));
                        Settings.System.putInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                        methodUltils.savePreBoolean(false, Contains.NIGHT_MODE, sharedPreferences);
                        imgNightMode.setImageResource(R.drawable.nightmode_off);
                    } else {
                        mContext.startService(new Intent(mContext, NightService.class));
                        Settings.System.putInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
                        methodUltils.savePreBoolean(true, Contains.NIGHT_MODE, sharedPreferences);
                        imgNightMode.setImageResource(R.drawable.nigtmode_on);
                    }
                    break;
                case R.id.cbBluetooth:
                    isCheckBluetooth = !isCheckBluetooth;
                    if (isCheckBluetooth) {
                        cbBluetooth.setImageResource(R.drawable.bluetooth_on);
                    } else {
                        cbBluetooth.setImageResource(R.drawable.bluetooth_off);
                    }
                    methodUltils.setBluetooth(isCheckBluetooth);
                    break;
                case R.id.autoBrightness:
                    if (getScreenBrightness() == 1) {
                        android.provider.Settings.System.putInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                        autoBrightness.setBackgroundResource(R.drawable.bg_black_alpha);

                    } else if (getScreenBrightness() == 0) {
                        android.provider.Settings.System.putInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
                        autoBrightness.setBackgroundResource(R.drawable.auto_brightness_on);
                    }

                    break;
                case R.id.lnlClock:
                    Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    sendMessage();
                    break;

                case R.id.lnlCaculator:
                    try {
                        intent = new Intent();
                        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_APP_CALCULATOR);
                        mContext.startActivity(intent);
                    } catch (Exception e) {
                        ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
                        PackageManager packageManager;
                        packageManager = mContext.getPackageManager();
                        List<PackageInfo> packs = packageManager.getInstalledPackages(0);
                        for (PackageInfo pi : packs) {
                            if (pi.packageName.toLowerCase().contains("calcul")) {
                                HashMap<String, Object> map = new HashMap<String, Object>();
                                map.put("appName", pi.applicationInfo.loadLabel(packageManager));
                                map.put("packageName", pi.packageName);
                                items.add(map);
                            }
                        }
                        if (items.size() >= 1) {
                            String packageName = (String) items.get(0).get("packageName");
                            Intent i = packageManager.getLaunchIntentForPackage(packageName);
                            if (i != null)
                                mContext.startActivity(i);
                        }
                    }
                    sendMessage();
                    break;

                case R.id.lnlCamera:
                    methodUltils.stopCamera();
                    Intent intentCamera = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
                    intentCamera.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intentCamera);
                    sendMessage();

                    break;
                case R.id.lnlOrientation:
                    if (methodUltils.isAutoOrientaitonEnable(mContext)) {
                        methodUltils.setAutoOrientationEnabled(mContext, false);
                        imgOrientation.setImageResource(R.drawable.lock_off);
                    } else {
                        methodUltils.setAutoOrientationEnabled(mContext, true);
                        imgOrientation.setImageResource(R.drawable.lock_on);
                    }
                    break;
                case R.id.cb3G:
                    go3G();
                    break;
                case R.id.cbWifi:
                    isCheckWifi = !isCheckWifi;
                    if (isCheckWifi) {
                        cbWifi.setImageResource(R.drawable.wifi_on);
                    } else cbWifi.setImageResource(R.drawable.wifi_off);
                    methodUltils.toggleWiFi(mContext, isCheckWifi);
                    break;
                case R.id.cbPlane:
                    goSetting();
                    break;
                case R.id.lnlFlash:
                    getCamera();
                    if (isCamera) {
                        if (checkFlashship(mContext)) {
                            if (isFlashOn) {
                                new Handler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imgFlash.setImageResource(R.drawable.flashlight_off);
                                            }
                                        }).run();
                                    }
                                });
                                startFlashLight(false);
                                isFlashOn = false;
                            } else {
                                new Handler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imgFlash.setImageResource(R.drawable.flashlight_on);
                                            }
                                        }).run();
                                    }
                                });
                                startFlashLight(true);
                                isFlashOn = true;
                            }
                        } else {
                            Toast.makeText(mContext, "no flash!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case R.id.tvCancelMusic:
                    rltMusic.setVisibility(View.GONE);
                    lnlControl.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.BounceInUp)
                            .duration(600)
                            .playOn(lnlControl);
                    break;

                case R.id.imgBackSong:
                    playPrev();
                    break;
                case R.id.imgStartPause:
                    try {
                        controlStartPause();
                    } catch (IllegalStateException e) {
                        musicSrv.setSong(0);
                        musicSrv.playSong();

                        tvNameArtist.setText(arrSongChoose.get(0).getArtist());
                        tvNameSong.setText(arrSongChoose.get(0).getTitle());

                        adapterAddSong.updateSongChoose(0);
                        imgStartPause.setImageResource(R.drawable.pause_music);
                    }
                    break;
                case R.id.imgNextSong:
                    playNext();
                    break;
            }
        }
    };

    protected int getScreenBrightness() {
        return Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, 0);
    }

    public void goSetting() {
        try {
            Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            sendMessage();
        } catch (Exception e) {
            Log.e("exception", e + "");
        }
    }

    private void sendMessage() {
        Intent intent = new Intent("closePanel");
        intent.putExtra("closePanel", "This is my message!");
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }


    @Override
    public void start() {
        try {
            musicSrv.go();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {
        try {
            playbackPaused = true;
            musicSrv.pausePlayer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean musicBound = false;

    @Override
    public int getDuration() {
        if (musicSrv != null && musicBound && musicSrv.isPng())
            return musicSrv.getDur();
        else return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (musicSrv != null && musicBound && musicSrv.isPng())
            return musicSrv.getPosn();
        else return 0;
    }

    @Override
    public void seekTo(int i) {
        musicSrv.seek(i);
    }

    @Override
    public boolean isPlaying() {
        if (musicSrv != null && musicBound)
            return musicSrv.isPng();
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }


    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.rltPlane3GWIFI:
                Log.d("thunt", "rltPlane3GWIFI");
                break;

            case R.id.rltSongs:
                Log.e("hoaii", "vaoday");
                methodUltils.runVibrateShort(mContext);
                lnlControl.setVisibility(View.GONE);
                rltMusic.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.BounceInUp)
                        .duration(700)
                        .playOn(rltMusic);
                break;
        }

        return true;
    }

    private BroadcastReceiver mReceiverBlutooth = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equalsIgnoreCase(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        // img_bluetooth.setVisibility(View.GONE);
                        isCheckBluetooth = false;
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        break;
                    case BluetoothAdapter.STATE_ON:
                        isCheckBluetooth = true;
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        break;
                }
            }
        }
    };

    BroadcastReceiver StrengthWifi = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setNetWorkState();
        }
    };

    private BroadcastReceiver ActionAirPlane = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0) {
                //img_plane.setVisibility(View.VISIBLE);
                cbPlane.setImageResource(R.drawable.airplane_on);
            } else {
                //  img_plane.setVisibility(View.GONE);
                cbPlane.setImageResource(R.drawable.airplane_off);
            }
        }
    };
}
