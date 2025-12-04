package com.example.musicclient;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.Random;
import javafx.util.Duration;


public class AudioPlayerFacade {

    private MediaPlayer currentPlayer;
    private Track currentTrack;
    private boolean shuffle = false;
    private boolean repeat = true;

    private double volume = 1.0;

    private final Random random = new Random();

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

    public MediaPlayer getCurrentPlayer() {
        return currentPlayer;
    }

    public Duration getCurrentTime() {
        if (currentPlayer != null) {
            return currentPlayer.getCurrentTime();
        }
        return Duration.ZERO;
    }

    public Duration getTotalDuration() {
        if (currentPlayer != null) {
            return currentPlayer.getTotalDuration();
        }
        return Duration.ZERO;
    }

    public void seek(Duration position) {
        if (currentPlayer != null && position != null) {
            currentPlayer.seek(position);
        }
    }

    public void setVolume(double volume) {
        this.volume = volume;
        if (currentPlayer != null) {
            currentPlayer.setVolume(volume);
        }
    }

    public double getVolume() {
        return volume;
    }

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


    public void resume() {
        if (currentPlayer != null) {
            currentPlayer.play();
        }
    }

    public void stop() {
        if (currentPlayer != null) {
            currentPlayer.stop();
        }
    }

    public void setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
    }

    public boolean isShuffle() {
        return shuffle;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public Track getCurrentTrack() {
        return currentTrack;
    }

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
