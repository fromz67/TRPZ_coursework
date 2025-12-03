package com.example.musicclient;

import java.util.List;

public class PlaylistTrackIterator implements TrackIterator {

    private final List<Track> tracks;
    private int currentIndex = -1;

    public PlaylistTrackIterator(List<Track> tracks) {
        this.tracks = tracks;
    }

    @Override
    public boolean hasNext() {
        return currentIndex + 1 < tracks.size();
    }

    @Override
    public Track next() {
        if (!hasNext()) {
            return null;
        }
        currentIndex++;
        return tracks.get(currentIndex);
    }

    @Override
    public boolean hasPrevious() {
        return currentIndex - 1 >= 0;
    }

    @Override
    public Track previous() {
        if (!hasPrevious()) {
            return null;
        }
        currentIndex--;
        return tracks.get(currentIndex);
    }

    @Override
    public Track current() {
        if (currentIndex < 0 || currentIndex >= tracks.size()) {
            return null;
        }
        return tracks.get(currentIndex);
    }
}
