package com.example.musicserver.repository;

import com.example.musicserver.entity.TrackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<TrackEntity, Long> {
}
