package com.duitsev.musicmetadataservice.domain;

import com.duitsev.musicmetadataservice.TestData;
import com.duitsev.musicmetadataservice.api.dto.AddArtistAliasRequest;
import com.duitsev.musicmetadataservice.api.dto.CreateTrackRequest;
import com.duitsev.musicmetadataservice.api.errors.AliasAlreadyExistsException;
import com.duitsev.musicmetadataservice.api.errors.ArtistNotFoundException;
import com.duitsev.musicmetadataservice.api.errors.TrackIsTooShortException;
import com.duitsev.musicmetadataservice.domain.entity.Artist;
import com.duitsev.musicmetadataservice.domain.entity.ArtistAlias;
import com.duitsev.musicmetadataservice.repository.ArtistAliasRepository;
import com.duitsev.musicmetadataservice.repository.ArtistRepository;
import com.duitsev.musicmetadataservice.repository.TrackRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional()
public class ArtistServiceIntegrationTest {

    @Autowired
    private ArtistRepository artistRepo;

    @Autowired
    private TrackRepository trackRepo;

    @Autowired
    private ArtistAliasRepository artistAliasRepo;

    @Autowired
    private ArtistService underTest;

    @BeforeEach
    public void cleanUp() {
        trackRepo.deleteAll();
        artistAliasRepo.deleteAll();
    }

    @Test
    public void getArtists_shouldReturnAllArtists() {
        var artists = underTest.getArtists();

        Assertions.assertThat(artists.size()).isEqualTo(5);
        Assertions.assertThat(artists.stream().map(Artist::getName)).containsExactlyInAnyOrderElementsOf(List.of(
                "Adele", "Beyonce", "Coldplay", "Drake", "Ed Sheeran"));
    }

    @Test
    public void addNewTrack_shouldCreateTrackForTheArtist() {
        var artist = artistRepo.findFirstByOrderByIdAsc();
        var created = underTest.addNewTrack(artist.getId(), new CreateTrackRequest("track1", "pop", 300));
        Assertions.assertThat(created.getArtistId()).isEqualTo(artist.getId());
        Assertions.assertThat(created.getTitle()).isEqualTo("track1");
        Assertions.assertThat(created.getGenre()).isEqualTo("pop");
        Assertions.assertThat(created.getLength()).isEqualTo(300);
    }

    @Test
    public void addNewTrack_shouldThrowIfArtistNotFound() {
        Assertions.assertThatThrownBy(() -> {
            underTest.addNewTrack(UUID.randomUUID(), new CreateTrackRequest("track1", "pop", 300));
        }).isInstanceOf(ArtistNotFoundException.class);
    }

    @Test
    public void addNewTrack_shouldThrowIfLengthIsTooShort() {
        var artist = artistRepo.findFirstByOrderByIdAsc();
        Assertions.assertThatThrownBy(() -> {
            underTest.addNewTrack(artist.getId(), new CreateTrackRequest("track1", "pop", 4));
        }).isInstanceOf(TrackIsTooShortException.class);
    }

    @Test
    public void getTracks_shouldReturnTracksForTheArtist() {
        var artist = artistRepo.findFirstByOrderByIdAsc();
        var track1 = trackRepo.save(TestData.track(artist.getId()));
        var track2 = trackRepo.save(TestData.track(artist.getId()));

        var tracks = underTest.getTracks(artist.getId());

        Assertions.assertThat(tracks).containsExactlyInAnyOrder(track1, track2);

    }

    @Test
    public void getTracks_shouldThrowIfArtistNotFound() {
        Assertions.assertThatThrownBy(() -> {
            underTest.getTracks(UUID.randomUUID());
        }).isInstanceOf(ArtistNotFoundException.class);
    }

    @Test
    public void addAlias_shouldCreateNewAliasForTheArtist() {
        var artist = artistRepo.findFirstByOrderByIdAsc();
        var aliasRequest = new AddArtistAliasRequest("alias1");
        underTest.addAlias(artist.getId(), aliasRequest);

        Assertions.assertThat(artist.getAliases().stream().map(ArtistAlias::getName)).contains(aliasRequest.alias());
    }

    @Test
    public void addAlias_shouldThrowIfAliasAlreadyExists() {
        var artist = artistRepo.findFirstByOrderByIdAsc();
        var aliasRequest = new AddArtistAliasRequest("alias1");
        underTest.addAlias(artist.getId(), aliasRequest);

        var aliases = artistAliasRepo.findByArtistId(artist.getId());
        Assertions.assertThat(aliases.size()).isEqualTo(1);

        Assertions.assertThatThrownBy(() -> {
            underTest.addAlias(artist.getId(), aliasRequest);
        }).isInstanceOf(AliasAlreadyExistsException.class);
    }

    @Test
    public void addAlias_shouldThrowIfArtistNotFound() {
        Assertions.assertThatThrownBy(() -> {
            underTest.addAlias(UUID.randomUUID(), new AddArtistAliasRequest("alias1"));
        }).isInstanceOf(ArtistNotFoundException.class);
    }

    @Test
    public void getArtistOfTheDay_shouldReturnArtistOfTheDay() {
        var rotation = new ArrayList<Artist>();

        rotation.add(underTest.getArtistOfTheDay(LocalDate.now()));
        rotation.add(underTest.getArtistOfTheDay(LocalDate.now().plus(1, ChronoUnit.DAYS)));
        rotation.add(underTest.getArtistOfTheDay(LocalDate.now().plus(2, ChronoUnit.DAYS)));
        rotation.add(underTest.getArtistOfTheDay(LocalDate.now().plus(3, ChronoUnit.DAYS)));
        rotation.add(underTest.getArtistOfTheDay(LocalDate.now().plus(4, ChronoUnit.DAYS)));

        Assertions.assertThat(rotation).containsExactlyInAnyOrderElementsOf(artistRepo.findAll());
    }
}

