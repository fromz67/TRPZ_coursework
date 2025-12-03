package com.example.musicserver.controller;

import com.example.musicserver.entity.PlaylistEntity;
import com.example.musicserver.service.PlaylistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playlists") // ← ВАЖЛИВО
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping
    public List<PlaylistEntity> getAll() {
        return playlistService.getAll();
    }

    @PostMapping
    public PlaylistEntity save(@RequestBody PlaylistEntity playlist) {
        return playlistService.save(playlist);
    }

    @DeleteMapping("/{id}")
    public void deletePlaylist(@PathVariable("id") Long id) {
        playlistService.deletePlaylist(id);
    }
}

