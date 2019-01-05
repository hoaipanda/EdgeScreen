package com.navigation.edgescreen.asyntask;

import android.content.Context;
import android.os.AsyncTask;

import com.navigation.edgescreen.data.RowListSong;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AsyncTackReadMusic extends AsyncTask<Void, Void, ArrayList<RowListSong>> {

    private GetSongsFromDevice getMusic;
    private GetMusicComplete getMusicComplete;

    public AsyncTackReadMusic(Context mContext, GetMusicComplete getMusicComplete) {
        getMusic = new GetSongsFromDevice(mContext);
        this.getMusicComplete = getMusicComplete;
    }

    @Override
    protected ArrayList<RowListSong> doInBackground(Void... params) {
        return getMusic.getData();
    }

    @Override
    protected void onPostExecute(ArrayList<RowListSong> rowListSongs) {
        super.onPostExecute(rowListSongs);
        Collections.sort(rowListSongs, new Comparator<RowListSong>(){
            public int compare(RowListSong a, RowListSong b){
                return a.getTitle().compareTo(b.getTitle());
            }
        });

        getMusicComplete.complete(rowListSongs);
    }
}