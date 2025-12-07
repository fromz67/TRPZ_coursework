package com.example.musicclient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

public class RestClient {

    private final String baseUrl;

    private final HttpClient httpClient;

    private final ObjectMapper objectMapper;

    public RestClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public List<Track> loadTracks() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/tracks"))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(
                        response.body(),
                        new TypeReference<List<Track>>() {}
                );
            } else {
                System.err.println("Failed to load tracks, status = " + response.statusCode());
            }
        } catch (Exception e) {
            System.err.println("Error loading tracks: " + e.getMessage());
        }
        return Collections.emptyList();
    }

    public List<Playlist> loadPlaylists() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/playlists"))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(
                        response.body(),
                        new TypeReference<List<Playlist>>() {}
                );
            } else {
                System.err.println("Failed to load playlists, status = " + response.statusCode());
            }
        } catch (Exception e) {
            System.err.println("Error loading playlists: " + e.getMessage());
        }
        return Collections.emptyList();
    }

    public void savePlaylist(Playlist playlist) {
        try {
            String json = objectMapper.writeValueAsString(playlist);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/playlists"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                Playlist updated = objectMapper.readValue(response.body(), Playlist.class);

                // Оновлення даних у локальному плейлисті
                playlist.setId(updated.getId());
                playlist.setName(updated.getName());

                playlist.getTracks().clear();
                playlist.getTracks().addAll(updated.getTracks());
            } else {
                System.err.println("Failed to save playlist, status = " + response.statusCode());
            }
        } catch (Exception e) {
            System.err.println("Error saving playlist: " + e.getMessage());
        }
    }

    public void deletePlaylist(Long playlistId) {
        if (playlistId == null) {
            return;
        }
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/playlists/" + playlistId))
                    .DELETE()
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 204 && response.statusCode() != 200) {
                System.err.println("Failed to delete playlist, status = " + response.statusCode());
            }
        } catch (Exception e) {
            System.err.println("Error deleting playlist: " + e.getMessage());
        }
    }

}
