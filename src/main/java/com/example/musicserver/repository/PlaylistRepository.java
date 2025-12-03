package com.example.musicserver.repository;

import com.example.musicserver.entity.PlaylistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<PlaylistEntity, Long> {
}
