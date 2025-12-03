package com.example.musicclient;

import java.util.ArrayList;
import java.util.List;

public class PlaylistManager {
    private final List<Playlist> playlists = new ArrayList<>();
    private Playlist currentPlaylist;

    public Playlist createPlaylist(String name) {
        Playlist playlist = new Playlist(name);
        playlists.add(playlist);
        if (currentPlaylist == null) {
            currentPlaylist = playlist;
        }
        return playlist;
    }

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
