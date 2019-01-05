package com.navigation.edgescreen.data;

public class RowListSong {
    private long id;
    private String title;
    private String artist;

    public RowListSong(long songID, String songTitle, String songArtist) {
        id = songID;
        title = songTitle;
        artist = songArtist;
    }

    public long getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }
}
