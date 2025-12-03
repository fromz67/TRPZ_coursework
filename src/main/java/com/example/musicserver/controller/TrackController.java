package com.example.musicserver.controller;

import com.example.musicserver.entity.TrackEntity;
import com.example.musicserver.service.TrackService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracks")
@CrossOrigin(origins = "*") // щоб клієнт з іншого origin міг звертатися
public class TrackController {

    private final TrackService trackService;

    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    @GetMapping
    public List<TrackEntity> getAllTracks() {
        return trackService.findAll();
    }

    @PostMapping
    public TrackEntity createTrack(@RequestBody TrackEntity track) {
        return trackService.save(track);
    }
}
