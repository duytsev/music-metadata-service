package com.duitsev.musicmetadataservice.api.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ArtistNotFoundException extends RuntimeException {

    public ArtistNotFoundException(UUID artistId) {
        super("Artist with id " + artistId + " not found");
    }
}

