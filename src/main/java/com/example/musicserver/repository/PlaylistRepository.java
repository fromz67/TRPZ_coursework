package com.example.musicserver.repository;

import com.example.musicserver.entity.PlaylistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

// Репозиторій для роботи з таблицею плейлистів
// Дає готові методи для збереження, видалення, пошуку та отримання плейлистів
public interface PlaylistRepository extends JpaRepository<PlaylistEntity, Long> {
}
