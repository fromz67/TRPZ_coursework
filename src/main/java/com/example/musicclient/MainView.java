package com.example.musicclient;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Slider;
import javafx.util.Duration;
import java.io.File;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.media.MediaPlayer;

public class MainView {

    private final BorderPane root;

    private final ListView<Track> trackListView;
    private final ListView<Playlist> playlistListView;

    private final Button playButton;
    private final Button pauseButton;
    private final Button nextButton;
    private final Button prevButton;
    private final Button importButton;

    private final Slider positionSlider;
    private final RestClient restClient;
    private final AudioPlayerFacade audioPlayer;
    private final MusicLibrary musicLibrary;
    private final PlaylistManager playlistManager;
    private final Button savePlaylistButton;

    private final Slider volumeSlider;
    private final ToggleButton shuffleButton;
    private final ToggleButton repeatButton;

    private final Button deletePlaylistButton;
    private final Button deleteTrackButton;
    private final Button addPlaylistButton;

    private final TextField searchField;


    public MainView() {
        this.root = new BorderPane();

        this.audioPlayer = new AudioPlayerFacade();
        this.musicLibrary = new MusicLibrary();
        this.playlistManager = new PlaylistManager();
        this.restClient = new RestClient("http://localhost:8080/api");

        this.trackListView = new ListView<>();
        this.playlistListView = new ListView<>();

        this.playButton = new Button("Play");
        this.pauseButton = new Button("Pause");
        this.nextButton = new Button("Next");
        this.prevButton = new Button("Prev");
        this.importButton = new Button("Import tracks");
        this.savePlaylistButton = new Button("Save playlist");

        this.volumeSlider = new Slider(0, 1, 1);
        this.shuffleButton = new ToggleButton("Shuffle");
        this.repeatButton = new ToggleButton("Repeat");

        this.deletePlaylistButton = new Button("Delete playlist");
        this.deleteTrackButton = new Button("Delete track");
        this.addPlaylistButton = new Button("Add playlist");

        this.positionSlider = new Slider(0, 100, 0);

        this.searchField = new TextField();
        this.searchField.setPromptText("Search tracks...");


        setupLayout();
        setupActions();
        loadDataFromServer();
    }

    public BorderPane getRoot() {
        return root;
    }

    private void setupLayout() {
        SplitPane centerPane = new SplitPane();
        centerPane.setOrientation(Orientation.HORIZONTAL);

        playlistListView.setPrefWidth(200);
        playlistListView.setPlaceholder(new Label("No playlists"));

        trackListView.setPlaceholder(new Label("No tracks"));

        VBox tracksBox = new VBox(5, searchField, trackListView);
        tracksBox.setPadding(new Insets(0, 0, 0, 5));

        centerPane.getItems().addAll(playlistListView, tracksBox);
        centerPane.setDividerPositions(0.3);

        HBox controls = new HBox(10,
                prevButton, playButton, pauseButton, nextButton,
                importButton, savePlaylistButton,
                addPlaylistButton, deleteTrackButton, deletePlaylistButton,
                new Label("Volume:"), volumeSlider,
                shuffleButton, repeatButton
        );
        controls.setPadding(new Insets(10));

        VBox bottomBox = new VBox(5, positionSlider, controls);
        bottomBox.setPadding(new Insets(5, 10, 10, 10));

        root.setCenter(centerPane);
        root.setBottom(bottomBox);
        root.setPadding(new Insets(10));

    }

    private void setupActions() {
        playButton.setOnAction(e -> {
            Track selected = trackListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                audioPlayer.play(selected);
                configurePositionSliderForCurrentTrack();
            }
        });

        searchField.textProperty().addListener((obs, oldText, newText) -> {
            applyTrackFilter(newText);
        });

        pauseButton.setOnAction(e -> audioPlayer.pause());

        nextButton.setOnAction(e -> {
            Playlist current = playlistManager.getCurrentPlaylist();
            Track next = audioPlayer.next(current);
            if (next != null) {
                trackListView.getSelectionModel().select(next);
                configurePositionSliderForCurrentTrack();
            }
        });

        prevButton.setOnAction(e -> {
            Playlist current = playlistManager.getCurrentPlaylist();
            Track prev = audioPlayer.previous(current);
            if (prev != null) {
                trackListView.getSelectionModel().select(prev);
                configurePositionSliderForCurrentTrack();
            }
        });

        positionSlider.setOnMouseReleased(e -> {
            MediaPlayer player = audioPlayer.getCurrentPlayer();
            if (player != null) {
                double seconds = positionSlider.getValue();
                player.seek(Duration.seconds(seconds));
            }
        });

        importButton.setOnAction(e -> importTracks());

        savePlaylistButton.setOnAction(e -> {
            Playlist current = playlistManager.getCurrentPlaylist();
            if (current != null) {
                restClient.savePlaylist(current);
            }
        });

        playlistListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                playlistManager.setCurrentPlaylist(newVal);
                searchField.clear();
                trackListView.getItems().setAll(newVal.getTracks());
            }
        });


        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            audioPlayer.setVolume(newVal.doubleValue());
        });

        shuffleButton.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            audioPlayer.setShuffle(isSelected);
        });

        repeatButton.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            audioPlayer.setRepeat(isSelected);
        });

        deletePlaylistButton.setOnAction(e -> {
            Playlist selected = playlistListView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                return;
            }

            restClient.deletePlaylist(selected.getId());

            playlistManager.removePlaylist(selected);
            playlistListView.getItems().remove(selected);

            Playlist current = playlistManager.getCurrentPlaylist();
            if (current != null) {
                trackListView.getItems().setAll(current.getTracks());
                playlistListView.getSelectionModel().select(current);
            } else {
                trackListView.getItems().clear();
            }
        });

        deleteTrackButton.setOnAction(e -> {
            Track selectedTrack = trackListView.getSelectionModel().getSelectedItem();
            if (selectedTrack == null) {
                return;
            }

            Playlist current = playlistManager.getCurrentPlaylist();
            if (current != null) {
                current.getTracks().remove(selectedTrack);
            }

            trackListView.getItems().remove(selectedTrack);
        });

        addPlaylistButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("New playlist");
            dialog.setHeaderText("Create new playlist");
            dialog.setContentText("Playlist name:");

            dialog.showAndWait().ifPresent(name -> {
                String trimmed = name.trim();
                if (trimmed.isEmpty()) {
                    return;
                }

                boolean exists = playlistManager.getPlaylists().stream()
                        .anyMatch(p -> trimmed.equalsIgnoreCase(p.getName()));

                if (exists) {
                    System.err.println("Playlist with this name already exists");
                    return;
                }

                Playlist newPlaylist = new Playlist(trimmed);
                playlistManager.getPlaylists().add(newPlaylist);
                playlistManager.setCurrentPlaylist(newPlaylist);

                playlistListView.getItems().add(newPlaylist);
                playlistListView.getSelectionModel().select(newPlaylist);

                trackListView.getItems().setAll(newPlaylist.getTracks());
            });
        });
    }

    private void importTracks() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select audio files");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Audio files", "*.mp3", "*.wav", "*.aac")
        );
        var files = fileChooser.showOpenMultipleDialog(root.getScene().getWindow());
        if (files != null) {
            for (File file : files) {
                Track track = new Track(
                        null,
                        file.getName(),
                        "Unknown artist",
                        "Unknown album",
                        file.getAbsolutePath()
                );
                musicLibrary.addTrack(track);
                trackListView.getItems().add(track);

                Playlist current = playlistManager.getCurrentPlaylist();
                if (current != null) {
                    current.addTrack(track);
                }
            }
        }
    }

    private void loadDataFromServer() {
        var tracksFromServer = restClient.loadTracks();
        for (Track t : tracksFromServer) {
            musicLibrary.addTrack(t);
        }
        trackListView.getItems().addAll(tracksFromServer);

        var playlistsFromServer = restClient.loadPlaylists();
        if (!playlistsFromServer.isEmpty()) {
            playlistListView.getItems().clear();
            playlistListView.getItems().addAll(playlistsFromServer);
            playlistManager.setCurrentPlaylist(playlistsFromServer.get(0));
            playlistListView.getSelectionModel().select(0);
            trackListView.getItems().setAll(playlistsFromServer.get(0).getTracks());
        }
    }

    private void configurePositionSliderForCurrentTrack() {
        MediaPlayer player = audioPlayer.getCurrentPlayer();
        if (player == null) {
            positionSlider.setDisable(true);
            positionSlider.setValue(0);
            return;
        }

        positionSlider.setDisable(false);

        player.setOnReady(() -> {
            Duration total = player.getTotalDuration();
            positionSlider.setMax(total.toSeconds());
        });

        player.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if (!positionSlider.isValueChanging()) {
                positionSlider.setValue(newTime.toSeconds());
            }
        });

        player.setOnEndOfMedia(() -> {
            if (repeatButton.isSelected()) {
                player.seek(Duration.ZERO);
                player.play();
                return;
            }

            Playlist current = playlistManager.getCurrentPlaylist();
            if (current != null) {
                Track next = audioPlayer.next(current);
                if (next != null) {
                    trackListView.getSelectionModel().select(next);
                    configurePositionSliderForCurrentTrack();
                }
            }
        });
    }


    private void applyTrackFilter(String query) {
        String trimmed = query == null ? "" : query.trim().toLowerCase();

        Playlist current = playlistManager.getCurrentPlaylist();
        if (current == null) {
            return;
        }

        if (trimmed.isEmpty()) {
            trackListView.getItems().setAll(current.getTracks());
            return;
        }

        var filtered = current.getTracks().stream()
                .filter(t -> {
                    String title = t.getTitle() != null ? t.getTitle().toLowerCase() : "";
                    String artist = t.getArtist() != null ? t.getArtist().toLowerCase() : "";
                    return title.contains(trimmed) || artist.contains(trimmed);
                })
                .toList();

        trackListView.getItems().setAll(filtered);
    }

}
