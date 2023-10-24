package com.duitsev.musicmetadataservice.api.dto;

import com.duitsev.musicmetadataservice.domain.entity.Artist;

public record ArtistDto(String id, String name) {

    public static ArtistDto from(Artist artist) {
        return new ArtistDto(artist.getId().toString(), artist.getName());
    }
}

