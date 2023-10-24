package com.duitsev.musicmetadataservice.api.dto;

import com.duitsev.musicmetadataservice.domain.entity.Artist;

import java.util.List;

public record GetArtistsResponse(List<ArtistDto> artists) {

    public static GetArtistsResponse from(List<Artist> artists) {
        return new GetArtistsResponse(artists.stream().map(ArtistDto::from).toList());
    }

}
