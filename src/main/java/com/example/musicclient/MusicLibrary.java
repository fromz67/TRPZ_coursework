package com.example.musicclient;

import java.util.ArrayList;
import java.util.List;

public class MusicLibrary {
    private final List<Track> tracks = new ArrayList<>();

    public void addTrack(Track track) {
        boolean exists = tracks.stream()
                .anyMatch(t -> t.getFilePath() != null
                        && t.getFilePath().equals(track.getFilePath()));
        if (!exists) {
            tracks.add(track);
        }
    }

    public List<Track> getAllTracks() {
        return tracks;
    }
}
