package com.example.musicserver.service;

import com.example.musicserver.entity.TrackEntity;
import com.example.musicserver.repository.TrackRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackService {

    private final TrackRepository trackRepository;

    public TrackService(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    public List<TrackEntity> findAll() {
        return trackRepository.findAll();
    }

    public TrackEntity save(TrackEntity track) {
        return trackRepository.save(track);
    }
}
