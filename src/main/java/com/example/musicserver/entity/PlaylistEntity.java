package com.example.musicserver.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "playlists")
public class PlaylistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "playlist_tracks",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "track_id")
    )
    private List<TrackEntity> tracks = new ArrayList<>();

    // Порожній конструктор для JPA
    public PlaylistEntity() {
    }

    // Конструктор для створення плейлиста з назвою
    public PlaylistEntity(String name) {
        this.name = name;
        this.createdAt = LocalDateTime.now();
    }

    // Геттери
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<TrackEntity> getTracks() {
        return tracks;
    }

    // Сеттери
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setTracks(List<TrackEntity> tracks) {
        this.tracks = tracks;
    }

    public void addTrack(TrackEntity track) {
        this.tracks.add(track);
    }

    public void removeTrack(TrackEntity track) {
        this.tracks.remove(track);
    }
}
