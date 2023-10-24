package com.duitsev.musicmetadataservice.api.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class TrackIsTooShortException extends RuntimeException {

    public TrackIsTooShortException() {
        super("Track is too short, minimum length is 5 seconds");
    }

}

