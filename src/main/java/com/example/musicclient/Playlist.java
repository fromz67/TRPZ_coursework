package com.example.musicclient;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Клас описує плейлист. Зберігає його назву, ідентифікатор
 * та список треків, які в ньому знаходяться.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Playlist {

    private Long id;

    private String name;

    private final List<Track> tracks = new ArrayList<>();

    // 🔹 Додай цей порожній конструктор для Jackson
    public Playlist() {
    }

    public Playlist(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Playlist(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addTrack(Track track) {
        boolean exists = tracks.stream()
                .anyMatch(t -> t.getFilePath() != null
                        && t.getFilePath().equals(track.getFilePath()));

        if (!exists) {
            tracks.add(track);
        }
    }

    @JsonIgnore
    public TrackIterator getIterator() {
        return new PlaylistTrackIterator(tracks);
    }

    @Override
    public String toString() {
        return name;
    }
}
