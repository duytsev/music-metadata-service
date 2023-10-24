package com.duitsev.musicmetadataservice.api.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class AliasAlreadyExistsException extends RuntimeException {

    public AliasAlreadyExistsException(String alias, String artistName) {
        super("Alias " + alias + " already exists for artist " + artistName + "");
    }

}

