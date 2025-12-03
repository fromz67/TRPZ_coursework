package com.example.musicserver.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tracks")
public class TrackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String artist;

    private String album;


    @Column(name = "file_path")
    private String filePath;

    public TrackEntity() {
    }

    public TrackEntity(String title, String artist, String album, String filePath) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.filePath = filePath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
