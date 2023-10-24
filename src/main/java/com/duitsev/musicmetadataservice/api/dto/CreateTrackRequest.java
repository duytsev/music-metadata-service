package com.duitsev.musicmetadataservice.api.dto;

import com.duitsev.musicmetadataservice.domain.entity.Track;

import java.util.UUID;

public record CreateTrackRequest(String title, String genre, int length) {

    public Track toEntity(UUID artistId) {
        Track track = new Track();
        track.setId(UUID.randomUUID());
        track.setTitle(title);
        track.setGenre(genre);
        track.setLength(length);
        track.setArtistId(artistId);
        return track;
    }
}

