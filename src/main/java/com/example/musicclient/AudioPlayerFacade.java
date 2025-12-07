package com.example.musicclient;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.Random;
import javafx.util.Duration;

/**
 * Фасад для роботи з аудіовідтворенням на основі JavaFX MediaPlayer.
 * Інкапсулює:
 *     створення та керування MediaPlayer для поточного треку;
 *     керування гучністю;
 *     режими Shuffle та Repeat;
 *     перемикання треків у межах плейліста (next/previous).
 * Використовується з боку UI як спрощений інтерфейс до аудіодвигуна.
 */
public class AudioPlayerFacade {

    private MediaPlayer currentPlayer;

    private Track currentTrack;

    private boolean shuffle = false;

    private boolean repeat = true;

    private double volume = 1.0;

    private final Random random = new Random();

    /**
     * Почати відтворення вказаного треку.
     * <p>
     * Якщо вже є активний MediaPlayer, він зупиняється
     * і створюється новий плеєр для переданого треку.
     */
    public void play(Track track) {
        try {
            if (track == null) return;

            if (currentPlayer != null) {
                currentPlayer.stop();
            }

            this.currentTrack = track;
            Media media = new Media(new File(track.getFilePath()).toURI().toString());
            currentPlayer = new MediaPlayer(media);
            currentPlayer.setVolume(volume);
            currentPlayer.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Повертає поточний MediaPlayer якщо він існує
     */
    public MediaPlayer getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Повертає поточний час відтворення треку.
     */
    public Duration getCurrentTime() {
        if (currentPlayer != null) {
            return currentPlayer.getCurrentTime();
        }
        return Duration.ZERO;
    }

    /**
     * Повертає загальну тривалість поточного треку
     */
    public Duration getTotalDuration() {
        if (currentPlayer != null) {
            return currentPlayer.getTotalDuration();
        }
        return Duration.ZERO;
    }

    /**
     * Переміщує позицію відтворення до вказаного моменту
     */
    public void seek(Duration position) {
        if (currentPlayer != null && position != null) {
            currentPlayer.seek(position);
        }
    }

    /**
     * Встановлює гучність відтворення.
     *
     * значення гучності в діапазоні від 0.0 до 1.0
     */
    public void setVolume(double volume) {
        this.volume = volume;
        if (currentPlayer != null) {
            currentPlayer.setVolume(volume);
        }
    }

    /**
     * Повертає поточне значення гучності.
     */
    public double getVolume() {
        return volume;
    }

    /**
     * Перемикає стан відтворення між "відтворення" та "пауза".
     *     Якщо трек зараз грає – ставиться на паузу.
     *     Якщо трек на паузі – відновлюється відтворення.
     * Якщо плеєр не ініціалізований – метод нічого не робить.
     */
    public void pause() {
        if (currentPlayer != null) {
            var status = currentPlayer.getStatus();
            if (status == MediaPlayer.Status.PLAYING) {
                currentPlayer.pause();
            } else if (status == MediaPlayer.Status.PAUSED) {
                currentPlayer.play();
            }
        }
    }

    /**
     * Явне відновлення відтворення поточного треку
     * (без перемикання режиму, на відміну від pause).
     */
    public void resume() {
        if (currentPlayer != null) {
            currentPlayer.play();
        }
    }

    /**
     * Зупиняє відтворення поточного треку.
     * Позиція відтворення скидається на початок.
     */
    public void stop() {
        if (currentPlayer != null) {
            currentPlayer.stop();
        }
    }

    /**
     * Вмикає або вимикає режим випадкового відтворення (Shuffle).
     *
     * вимкнути\увімкнути Shuffle,
     */
    public void setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
    }

    /**
     * Повертає поточний стан режиму Shuffle.
     */
    public boolean isShuffle() {
        return shuffle;
    }

    /**
     * Вмикає або вимикає режим повтору поточного треку (Repeat).
     */
    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    /**
     * Повертає поточний стан режиму Repeat.
     */
    public boolean isRepeat() {
        return repeat;
    }

    /**
     * Повертає поточний трек, який відтворюється або останнім відтворювався.
     */
    public Track getCurrentTrack() {
        return currentTrack;
    }

    /**
     * Перемикається на наступний трек у вказаному плейлісті.
     *     Якщо ввімкнено Shuffle – обирається випадковий трек, відмінний від поточного.
     *     Якщо Shuffle вимкнено – використовується ітератор плейліста.
     *     При досягненні кінця списку відбувається перехід на перший трек.
     */
    public Track next(Playlist playlist) {
        if (playlist == null || playlist.getTracks().isEmpty()) {
            return null;
        }

        var tracks = playlist.getTracks();

        if (shuffle && tracks.size() > 1) {
            int currentIndex = currentTrack != null ? tracks.indexOf(currentTrack) : -1;
            int nextIndex;
            do {
                nextIndex = random.nextInt(tracks.size());
            } while (nextIndex == currentIndex);
            Track nextTrack = tracks.get(nextIndex);
            play(nextTrack);
            return nextTrack;
        }

        TrackIterator iterator = playlist.getIterator();

        if (currentTrack == null) {
            if (iterator.hasNext()) {
                Track first = iterator.next();
                play(first);
                return first;
            }
            return null;
        }

        while (iterator.hasNext()) {
            Track t = iterator.next();
            if (t == currentTrack) {
                break;
            }
        }

        if (!iterator.hasNext()) {
            TrackIterator restartIterator = playlist.getIterator();
            if (restartIterator.hasNext()) {
                Track first = restartIterator.next();
                play(first);
                return first;
            }
            return null;
        }

        Track nextTrack = iterator.next();
        play(nextTrack);
        return nextTrack;
    }

    /**
     * Перемикається на попередній трек у вказаному плейлісті.
     *     Якщо ввімкнено Shuffle – обирається випадковий трек, відмінний від поточного.
     *     Якщо Shuffle вимкнено – використовується двобічний ітератор плейліста.
     *     При переході назад з першого елемента – перехід на останній трек.
     */
    public Track previous(Playlist playlist) {
        if (playlist == null || playlist.getTracks().isEmpty()) {
            return null;
        }

        var tracks = playlist.getTracks();

        if (shuffle && tracks.size() > 1) {
            int currentIndex = currentTrack != null ? tracks.indexOf(currentTrack) : -1;
            int prevIndex;
            do {
                prevIndex = random.nextInt(tracks.size());
            } while (prevIndex == currentIndex);
            Track prevTrack = tracks.get(prevIndex);
            play(prevTrack);
            return prevTrack;
        }

        TrackIterator iterator = playlist.getIterator();

        if (currentTrack == null) {
            Track last = null;
            while (iterator.hasNext()) {
                last = iterator.next();
            }
            if (last != null) {
                play(last);
            }
            return last;
        }

        while (iterator.hasNext()) {
            Track t = iterator.next();
            if (t == currentTrack) {
                break;
            }
        }

        if (iterator.hasPrevious()) {
            Track prevTrack = iterator.previous();
            play(prevTrack);
            return prevTrack;
        } else {
            Track last = null;
            while (iterator.hasNext()) {
                last = iterator.next();
            }
            if (last != null) {
                play(last);
            }
            return last;
        }
    }
}
