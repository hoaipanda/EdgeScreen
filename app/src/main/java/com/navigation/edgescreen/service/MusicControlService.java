package com.navigation.edgescreen.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.navigation.edgescreen.R;
import com.navigation.edgescreen.data.RowListSong;
import com.navigation.edgescreen.fragment.NotyFragment;

import java.util.ArrayList;
import java.util.Random;

public class MusicControlService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    //media player
    public static MediaPlayer player;
    //song list
    private ArrayList<RowListSong> songs;
    //current position
    private int songPosn;
    //binder
    private final IBinder musicBind = new MusicBinder();
    //title of current song
    private String songTitle = "";
    //notification id
    private static final int NOTIFY_ID = 1;
    //shuffle flag and random
    private boolean shuffle = false;
    private Random rand;
    private NotificationManagerCompat notification;
    private NotificationCompat.Builder builder;
    private Notification mNotification;
    private RemoteViews views;
    private RemoteViews smallViews;
    private RowListSong songRun;
    private PendingIntent pendingIntent;
    private static final int ID_NO_MEDIA = 30;
    public static final String PREV_ACTION = "com.notification.notify.ios11.prev";
    public static final String PLAY_ACTION = "com.notification.notify.ios11.play";
    public static final String NEXT_ACTION = "com.notification.notify.ios11.next";
    public static final String REMOVE_SONG = "com.notification.notify.ios11.removesong";
    public static final String CLICK_ITEM = "com.notification.notify.ios11.clicksong";
    public static final String STOP_FOREGROUND_ACTION = "com.notification.notify.ios11.stopforeground";
    private NotyFragment controlPanelService;

    public void onCreate() {
        //create the service
        super.onCreate();
        //initialize position
        songPosn = 0;
        //random
        rand = new Random();
        //create player
        player = new MediaPlayer();
        //initialize
        initMusicPlayer();
        controlPanelService = new NotyFragment();
        notification = NotificationManagerCompat.from(this);
    }

    public void initMusicPlayer() {
        //set player properties
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //set listeners
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    //pass song list
    public void setList(ArrayList<RowListSong> theSongs) {
        songs = theSongs;
    }

    //binder
    public class MusicBinder extends Binder {
        public MusicControlService getService() {
            return MusicControlService.this;
        }
    }

    //activity will bind to service
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    //release resources when unbind
    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    //play a song
    public void playSong() {
        try {
            player.reset();
            //get song
            RowListSong playSong = songs.get(songPosn);
            songRun = playSong;
            //get title
            songTitle = playSong.getTitle();
            //get id
            long currSong = playSong.getID();
            //set uri
            Uri trackUri = ContentUris.withAppendedId(
                    android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    currSong);
            //set the data source
            try {
                player.setDataSource(getApplicationContext(), trackUri);
            } catch (Exception e) {
                Log.e("MUSIC SERVICE", "Error setting data source", e);
            }
            player.prepareAsync();

            controlPanelService.setSong(playSong.getTitle(), playSong.getArtist(), songPosn);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //set the song
    public void setSong(int songIndex) {
        songPosn = songIndex;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //check if playback has reached the end of a track
        if (player.getCurrentPosition() > 0) {
            mp.reset();
            playNext();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.v("MUSIC PLAYER", "Playback Error");
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //start playback
        mp.start();
        customNotification(songRun);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return START_NOT_STICKY;
        }
        // pause
        if (PLAY_ACTION.equals(intent.getAction())) {
            if (isPng()) {
                controlPanelService.controlStartPause();
                views.setImageViewResource(R.id.btn_image_play, R.drawable.ic_play_gray);
                smallViews.setImageViewResource(R.id.btn_image_play, R.drawable.ic_play_gray);
                startForeground(ID_NO_MEDIA, mNotification);
            } else {
                playSong();
                views.setImageViewResource(R.id.btn_image_play, R.drawable.ic_pause_gray);
                smallViews.setImageViewResource(R.id.btn_image_play, R.drawable.ic_pause_gray);
                startForeground(ID_NO_MEDIA, mNotification);
            }
        } else if (STOP_FOREGROUND_ACTION.equals(intent.getAction())) {
            stopForeground(false);
            stopService(intent);
            controlPanelService.pause();
            notification.cancel(ID_NO_MEDIA);
        } else if (NEXT_ACTION.equals(intent.getAction())) {
            playNext();
        } else if (PREV_ACTION.equals(intent.getAction())) {
            playPrev();
        }

        return START_STICKY;
    }

    //playback methods
    public int getPosn() {
        return player.getCurrentPosition();
    }

    public int getDur() {
        return player.getDuration();
    }

    public boolean isPng() {
        return player.isPlaying();
    }

    public void pausePlayer() {
        player.pause();
    }

    public void seek(int posn) {
        player.seekTo(posn);
    }

    public void go() {
        player.start();
    }

    //skip to previous track
    public void playPrev() {
        songPosn--;
        if (songPosn < 0) songPosn = songs.size() - 1;
        playSong();
    }

    //skip to next
    public void playNext() {
        if (shuffle) {
            int newSong = songPosn;
            while (newSong == songPosn) {
                newSong = rand.nextInt(songs.size());
            }
            songPosn = newSong;
        } else {
            songPosn++;
            if (songPosn >= songs.size()) songPosn = 0;
        }
        playSong();
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        notification.cancel(ID_NO_MEDIA);
    }

    //toggle shuffle
    public void setShuffle() {
        if (shuffle) shuffle = false;
        else shuffle = true;
    }


    private void changeDisPlayNo(RowListSong song) {
        views.setTextViewText(R.id.txt_name_no, song.getTitle());
        views.setTextViewText(R.id.txt_artist_no, song.getArtist());
        smallViews.setTextViewText(R.id.txt_name_no, song.getTitle());
        smallViews.setTextViewText(R.id.txt_artist_no, song.getArtist());
        views.setImageViewResource(R.id.img_avatar_notification, R.drawable.icon144_notificationios11);
        smallViews.setImageViewResource(R.id.img_avatar_notification, R.drawable.icon144_notificationios11);
    }

    private void putPending() {
        Intent intentNotification = new Intent();
        pendingIntent = PendingIntent.getActivity(this, 0, intentNotification, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
    }


    public void customNotification(RowListSong song) {
        views = new RemoteViews(getPackageName(), R.layout.custom_notification_big);
        smallViews = new RemoteViews(getPackageName(), R.layout.custom_notification_small);
        builder = new NotificationCompat.Builder(this);
        putPending();

        Intent intentPrev = new Intent();
        intentPrev.setClass(this, MusicControlService.class);
        intentPrev.setAction(PREV_ACTION);
        PendingIntent pendingPrev = PendingIntent.getService(this, 0, intentPrev, 0);

        Intent intentPlay = new Intent();
        intentPlay.setClass(this, MusicControlService.class);
        intentPlay.setAction(PLAY_ACTION);
        PendingIntent pendingPlay = PendingIntent.getService(this, 0, intentPlay, 0);

        Intent intentNext = new Intent();
        intentNext.setClass(this, MusicControlService.class);
        intentNext.setAction(NEXT_ACTION);

        PendingIntent pendingNext = PendingIntent.getService(this, 0, intentNext, 0);

        Intent intentClose = new Intent();
        intentClose.setClass(this, MusicControlService.class);
        intentClose.setAction(STOP_FOREGROUND_ACTION);
        PendingIntent pendingClose = PendingIntent.getService(this, 0, intentClose, 0);

        changeDisPlayNo(song);
        views.setOnClickPendingIntent(R.id.btn_image_prev, pendingPrev);
        views.setOnClickPendingIntent(R.id.btn_image_play, pendingPlay);
        views.setOnClickPendingIntent(R.id.btn_image_next, pendingNext);
        views.setOnClickPendingIntent(R.id.img_close, pendingClose);

        smallViews.setOnClickPendingIntent(R.id.btn_image_play, pendingPlay);
        smallViews.setOnClickPendingIntent(R.id.btn_image_next, pendingNext);
        smallViews.setOnClickPendingIntent(R.id.img_close, pendingClose);

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContent(smallViews);
        builder.setCustomBigContentView(views);
        builder.setContentIntent(pendingIntent);
        mNotification = builder.build();
        startForeground(ID_NO_MEDIA, mNotification);
    }
}