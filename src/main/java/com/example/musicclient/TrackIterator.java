package com.example.musicclient;

public interface TrackIterator {
    boolean hasNext();
    Track next();
    boolean hasPrevious();
    Track previous();
    Track current();
}
