package com.duitsev.musicmetadataservice;

import com.duitsev.musicmetadataservice.domain.entity.Artist;
import com.duitsev.musicmetadataservice.domain.entity.ArtistAlias;
import com.duitsev.musicmetadataservice.domain.entity.Track;
import com.github.javafaker.Faker;

import java.util.UUID;

public class TestData {

    public static Track track(UUID artistId) {
        Faker faker = new Faker();
        Track track = new Track();
        track.setId(UUID.randomUUID());
        track.setTitle(faker.lorem().word());
        track.setGenre(faker.music().genre());
        track.setLength(faker.random().nextInt(100, 1000));
        track.setArtistId(artistId);
        return track;
    }

    public static Artist artist() {
        Faker faker = new Faker();
        Artist artist = new Artist();
        artist.setId(UUID.randomUUID());
        artist.setName(faker.name().fullName());
        artist.setIdx(faker.random().nextLong());
        return artist;
    }

    public static ArtistAlias artistAlias(UUID artistId) {
        Faker faker = new Faker();
        ArtistAlias artistAlias = new ArtistAlias();
        artistAlias.setId(UUID.randomUUID());
        artistAlias.setName(faker.name().fullName());
        artistAlias.setArtistId(artistId);
        return artistAlias;
    }
}

