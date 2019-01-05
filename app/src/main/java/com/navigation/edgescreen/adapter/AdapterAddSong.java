package com.navigation.edgescreen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.navigation.edgescreen.R;
import com.navigation.edgescreen.data.RowListSong;

import java.util.ArrayList;

public class AdapterAddSong extends BaseAdapter {

    //song list and layout
    private ArrayList<RowListSong> songs;
    private LayoutInflater songInf;
    private int songChoose;

    //constructor
    public AdapterAddSong(Context c, ArrayList<RowListSong> theSongs, int songChoose) {
        songs = theSongs;
        this.songChoose = songChoose;
        songInf = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //map to song layout
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = songInf.inflate(R.layout.item_show_song, parent, false);
            viewHolder.songView = convertView.findViewById(R.id.song_title);
            viewHolder.artistView = convertView.findViewById(R.id.song_artist);
            viewHolder.imgChoose = (ImageView) convertView.findViewById(R.id.imgChoose);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position == songChoose) {
            viewHolder.imgChoose.setVisibility(View.VISIBLE);
        } else viewHolder.imgChoose.setVisibility(View.GONE);

        RowListSong currSong = songs.get(position);
        viewHolder.songView.setText(currSong.getTitle());
        viewHolder.artistView.setText(currSong.getArtist());


        return convertView;
    }

    private class ViewHolder {
        TextView songView;
        TextView artistView;
        ImageView imgChoose;
    }

    public void updateSongChoose(int songChoose) {
        this.songChoose = songChoose;
        notifyDataSetChanged();
    }

}