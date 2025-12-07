package com.example.musicclient;

import java.util.List;

public class PlaylistTrackIterator implements TrackIterator {

    // Список треків, по якому відбувається переміщення
    private final List<Track> tracks;

    // Поточний індекс треку; -1 означає, що ще нічого не вибрано
    private int currentIndex = -1;

    // Приймає список треків, які потрібно перебирати
    public PlaylistTrackIterator(List<Track> tracks) {
        this.tracks = tracks;
    }

    // Перевіряє, чи існує наступний трек
    @Override
    public boolean hasNext() {
        return currentIndex + 1 < tracks.size();
    }

    // Переходить до наступного треку і повертає його
    // Якщо наступного немає — повертає null
    @Override
    public Track next() {
        if (!hasNext()) {
            return null;
        }
        currentIndex++;
        return tracks.get(currentIndex);
    }

    // Перевіряє, чи існує попередній трек
    @Override
    public boolean hasPrevious() {
        return currentIndex - 1 >= 0;
    }

    // Повертає попередній трек і зміщує індекс назад
    // Якщо попереднього немає — повертає null
    @Override
    public Track previous() {
        if (!hasPrevious()) {
            return null;
        }
        currentIndex--;
        return tracks.get(currentIndex);
    }

    // Повертає поточний трек або null, якщо індекс неправильний
    @Override
    public Track current() {
        if (currentIndex < 0 || currentIndex >= tracks.size()) {
            return null;
        }
        return tracks.get(currentIndex);
    }
}
