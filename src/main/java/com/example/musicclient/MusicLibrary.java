package com.example.musicclient;

import java.util.ArrayList;
import java.util.List;

/**
 * Локальна музична бібліотека, яка зберігає всі треки,
 * що були імпортовані або отримані з сервера.
 */
public class MusicLibrary {

    /**
     * Список усіх треків, що зберігаються у бібліотеці.
     */
    private final List<Track> tracks = new ArrayList<>();

    /**
     * Додає новий трек у бібліотеку.
     * Перед додаванням перевіряє, чи немає вже треку з таким самим шляхом до файлу,
     * щоб уникнути дублювання.
     */
    public void addTrack(Track track) {
        boolean exists = tracks.stream()
                .anyMatch(t -> t.getFilePath() != null
                        && t.getFilePath().equals(track.getFilePath()));
        if (!exists) {
            tracks.add(track);
        }
    }
}
