package com.navigation.edgescreen.asyntask;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.navigation.edgescreen.data.RowListSong;

import java.util.ArrayList;

public class GetSongsFromDevice {
    private ContentResolver mResolver;
    private Context mContext;
    private ArrayList<RowListSong> songList;
    public static final String[] COLUMN = {"_data", "title", "duration", "is_music", "artist", "album_id"};

    public GetSongsFromDevice(Context mContext) {
        this.mContext = mContext;
        mResolver = mContext.getContentResolver();
    }

    public ArrayList<RowListSong> getData() {
        songList = new ArrayList<>();
        ContentResolver musicResolver = mContext.getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        //iterate over results if valid
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songList.add(new RowListSong(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }

        return songList;
    }
}
