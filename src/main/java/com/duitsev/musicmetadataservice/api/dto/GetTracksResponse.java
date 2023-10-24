package com.duitsev.musicmetadataservice.api.dto;

import com.duitsev.musicmetadataservice.domain.entity.Track;

import java.util.List;

public record GetTracksResponse(List<TrackDto> tracks) {

    public static GetTracksResponse from(List<Track> tracks) {
        return new GetTracksResponse(tracks.stream().map(track -> new TrackDto(
                track.getId().toString(),
                track.getTitle(),
                track.getGenre(),
                track.getLength()
        )).toList());
    }
}

record TrackDto(String id, String title, String genre, int length) {

}
