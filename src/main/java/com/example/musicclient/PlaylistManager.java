package com.example.musicclient;

import java.util.ArrayList;
import java.util.List;

public class PlaylistManager {

    private final List<Playlist> playlists = new ArrayList<>();

    private Playlist currentPlaylist;

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public Playlist getCurrentPlaylist() {
        return currentPlaylist;
    }

    public void setCurrentPlaylist(Playlist playlist) {
        this.currentPlaylist = playlist;
    }

    public void removePlaylist(Playlist playlist) {
        playlists.remove(playlist);
        if (currentPlaylist == playlist) {
            if (playlists.isEmpty()) {
                currentPlaylist = null;
            } else {
                currentPlaylist = playlists.get(0);
            }
        }
    }

}
