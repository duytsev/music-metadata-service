package com.duitsev.musicmetadataservice.api.dto;

import com.duitsev.musicmetadataservice.domain.entity.Track;

public record CreateTrackResponse(String id, String title, String genre, int length) {

    public static CreateTrackResponse from(Track track) {
        return new CreateTrackResponse(
                track.getId().toString(),
                track.getTitle(),
                track.getGenre(),
                track.getLength());
    }
}

