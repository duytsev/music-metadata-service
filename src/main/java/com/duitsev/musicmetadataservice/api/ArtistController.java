package com.duitsev.musicmetadataservice.api;

import com.duitsev.musicmetadataservice.api.dto.AddArtistAliasRequest;
import com.duitsev.musicmetadataservice.api.dto.AddArtistAliasResponse;
import com.duitsev.musicmetadataservice.api.dto.ArtistDto;
import com.duitsev.musicmetadataservice.api.dto.CreateTrackRequest;
import com.duitsev.musicmetadataservice.api.dto.CreateTrackResponse;
import com.duitsev.musicmetadataservice.api.dto.GetArtistsResponse;
import com.duitsev.musicmetadataservice.api.dto.GetTracksResponse;
import com.duitsev.musicmetadataservice.domain.ArtistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@RestController()
@RequestMapping("artists")
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping()
    public ResponseEntity<GetArtistsResponse> getArtists() {
        var artists = artistService.getArtists();
        return ResponseEntity.ok(GetArtistsResponse.from(artists));
    }

    @GetMapping("{artistId}/tracks")
    public ResponseEntity<GetTracksResponse> getTracks(@PathVariable UUID artistId) {
        var tracks = artistService.getTracks(artistId);
        return ResponseEntity.ok(GetTracksResponse.from(tracks));
    }

    @PostMapping("{artistId}/tracks")
    public ResponseEntity<CreateTrackResponse> addTrack(@PathVariable UUID artistId,
                                                        @RequestBody CreateTrackRequest createTrackRequest) {
        var track = artistService.addNewTrack(artistId, createTrackRequest);
        return ResponseEntity.ok(CreateTrackResponse.from(track));
    }

    @PostMapping("{artistId}/aliases")
    public ResponseEntity<AddArtistAliasResponse> addAlias(@PathVariable UUID artistId,
                                                           @RequestBody AddArtistAliasRequest addArtistAliasRequest) {
        var artist = artistService.addAlias(artistId, addArtistAliasRequest);
        return ResponseEntity.ok(AddArtistAliasResponse.from(artist));
    }

    @GetMapping("oftheday")
    public ResponseEntity<ArtistDto> getArtistOfTheDay() {
        var artist = artistService.getArtistOfTheDay(LocalDate.now());
        return ResponseEntity.ok(ArtistDto.from(artist));
    }

}

