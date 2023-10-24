package com.duitsev.musicmetadataservice.domain;

import com.duitsev.musicmetadataservice.api.dto.AddArtistAliasRequest;
import com.duitsev.musicmetadataservice.api.dto.CreateTrackRequest;
import com.duitsev.musicmetadataservice.api.errors.AliasAlreadyExistsException;
import com.duitsev.musicmetadataservice.api.errors.ArtistNotFoundException;
import com.duitsev.musicmetadataservice.api.errors.TrackIsTooShortException;
import com.duitsev.musicmetadataservice.domain.entity.Artist;
import com.duitsev.musicmetadataservice.domain.entity.Track;
import com.duitsev.musicmetadataservice.repository.ArtistAliasRepository;
import com.duitsev.musicmetadataservice.repository.ArtistRepository;
import com.duitsev.musicmetadataservice.repository.TrackRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ArtistService {

    private final ArtistRepository artistRepository;

    private final TrackRepository trackRepository;

    private final ArtistAliasRepository artistAliasRepository;

    public ArtistService(ArtistRepository artistRepository, TrackRepository trackRepository,
                         ArtistAliasRepository artistAliasRepository) {
        this.artistRepository = artistRepository;
        this.trackRepository = trackRepository;
        this.artistAliasRepository = artistAliasRepository;
    }

    public List<Artist> getArtists() {
        return artistRepository.findAll();
    }

    public Track addNewTrack(UUID artistId, CreateTrackRequest createTrackRequest) {
        validateNewTrack(createTrackRequest);
        var artist = findArtistOrThrow(artistId);
        return trackRepository.save(createTrackRequest.toEntity(artist.getId()));
    }

    public List<Track> getTracks(UUID artistId) {
        var artist = findArtistOrThrow(artistId);
        return trackRepository.findByArtistId(artist.getId());
    }

    public Artist addAlias(UUID artistId, AddArtistAliasRequest addArtistAliasRequest) {
        var artist = findArtistOrThrow(artistId);
        try {
            artistAliasRepository.saveAndFlush(addArtistAliasRequest.toEntity(artist.getId()));
            return artist;
        } catch (DataIntegrityViolationException e) {
            throw new AliasAlreadyExistsException(addArtistAliasRequest.alias(), artist.getName());
        }
    }

    public Artist getArtistOfTheDay(LocalDate currentDate) {
        var dayNumber = ChronoUnit.DAYS.between(LocalDate.EPOCH, currentDate);
        var artistCount = artistRepository.count();
        var artistIndex = dayNumber % artistCount;
        return artistRepository.findByIdx((int) artistIndex);
    }

    private void validateNewTrack(CreateTrackRequest createTrackRequest) {
        if (createTrackRequest.length() < 5) {
            throw new TrackIsTooShortException();
        }
    }

    private Artist findArtistOrThrow(UUID artistId) {
        return artistRepository.findById(artistId).orElseThrow(() -> new ArtistNotFoundException(artistId));
    }
}

