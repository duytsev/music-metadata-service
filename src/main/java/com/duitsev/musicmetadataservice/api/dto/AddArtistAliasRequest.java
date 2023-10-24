package com.duitsev.musicmetadataservice.api.dto;

import com.duitsev.musicmetadataservice.domain.entity.ArtistAlias;

import java.util.UUID;

public record AddArtistAliasRequest(String alias) {

    public ArtistAlias toEntity(UUID artistId) {
        ArtistAlias artistAlias = new ArtistAlias();
        artistAlias.setId(UUID.randomUUID());
        artistAlias.setName(alias);
        artistAlias.setArtistId(artistId);
        return artistAlias;
    }
}

