package com.example.musicserver.repository;

import com.example.musicserver.entity.TrackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

// Репозиторій для роботи з таблицею треків
// Дає готові методи для збереження, видалення, пошуку та отримання треків
public interface TrackRepository extends JpaRepository<TrackEntity, Long> {
}
