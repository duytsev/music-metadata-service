package com.duitsev.musicmetadataservice.api.dto;

import com.duitsev.musicmetadataservice.domain.entity.Artist;
import com.duitsev.musicmetadataservice.domain.entity.ArtistAlias;

public record AddArtistAliasResponse(String name, String[] aliases) {

    public static AddArtistAliasResponse from(Artist artist) {
        return new AddArtistAliasResponse(artist.getName(),
                artist.getAliases().stream().map(ArtistAlias::getName).toArray(String[]::new));
    }
}

