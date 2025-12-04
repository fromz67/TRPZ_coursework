package com.example.musicserver.service;

import com.example.musicserver.entity.PlaylistEntity;
import com.example.musicserver.repository.PlaylistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;

    public PlaylistService(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    public List<PlaylistEntity> getAll() {
        return playlistRepository.findAll();
    }

    public PlaylistEntity save(PlaylistEntity playlist) {
        return playlistRepository.save(playlist);
    }


    @Transactional
    public void deletePlaylist(Long id) {
        PlaylistEntity playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found: " + id));

        playlist.getTracks().clear();

        playlistRepository.delete(playlist);
    }
}
