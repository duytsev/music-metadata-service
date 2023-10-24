package com.duitsev.musicmetadataservice.api;

import com.duitsev.musicmetadataservice.TestData;
import com.duitsev.musicmetadataservice.api.dto.AddArtistAliasRequest;
import com.duitsev.musicmetadataservice.api.dto.CreateTrackRequest;
import com.duitsev.musicmetadataservice.api.errors.AliasAlreadyExistsException;
import com.duitsev.musicmetadataservice.api.errors.ArtistNotFoundException;
import com.duitsev.musicmetadataservice.api.errors.TrackIsTooShortException;
import com.duitsev.musicmetadataservice.domain.ArtistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArtistController.class)
public class ArtistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArtistService artistService;

    @Test
    public void getArtists_shouldReturnArtists() throws Exception {
        var artist1 = TestData.artist();
        var artist2 = TestData.artist();
        var artists = List.of(artist1, artist2);
        when(artistService.getArtists()).thenReturn(artists);

        mockMvc.perform(get("/artists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.artists").isArray())
                .andExpect(jsonPath("$.artists.length()").value(2))
                .andExpect(jsonPath("$.artists[0].id").value(artist1.getId().toString()))
                .andExpect(jsonPath("$.artists[0].name").value(artist1.getName()))
                .andExpect(jsonPath("$.artists[1].id").value(artist2.getId().toString()))
                .andExpect(jsonPath("$.artists[1].name").value(artist2.getName()));
    }

    @Test
    public void getTracks_shouldReturnTracks() throws Exception {
        var artistId = UUID.randomUUID();
        var track1 = TestData.track(artistId);
        var track2 = TestData.track(artistId);
        var tracks = List.of(track1, track2);
        when(artistService.getTracks(artistId)).thenReturn(tracks);

        mockMvc.perform(get("/artists/{artistId}/tracks", artistId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tracks").isArray())
                .andExpect(jsonPath("$.tracks.length()").value(2))
                .andExpect(jsonPath("$.tracks[0].id").value(track1.getId().toString()))
                .andExpect(jsonPath("$.tracks[0].title").value(track1.getTitle()))
                .andExpect(jsonPath("$.tracks[0].genre").value(track1.getGenre()))
                .andExpect(jsonPath("$.tracks[0].length").value(track1.getLength()))
                .andExpect(jsonPath("$.tracks[1].id").value(track2.getId().toString()))
                .andExpect(jsonPath("$.tracks[1].title").value(track2.getTitle()))
                .andExpect(jsonPath("$.tracks[1].genre").value(track2.getGenre()))
                .andExpect(jsonPath("$.tracks[1].length").value(track2.getLength()));
    }

    @Test
    public void getTracks_shouldReturn404IfArtistNotFound() throws Exception {
        var artistId = UUID.randomUUID();
        when(artistService.getTracks(artistId)).thenThrow(new ArtistNotFoundException(artistId));

        mockMvc.perform(get("/artists/{artistId}/tracks", artistId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createTrack_shouldCreateNewTrack() throws Exception {
        var artistId = UUID.randomUUID();
        var createTrackRequest = new CreateTrackRequest("track1", "pop", 300);
        var track = TestData.track(artistId);
        when(artistService.addNewTrack(artistId, createTrackRequest)).thenReturn(track);

        mockMvc.perform(post("/artists/{artistId}/tracks", artistId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createTrackRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(track.getId().toString()))
                .andExpect(jsonPath("$.title").value(track.getTitle()))
                .andExpect(jsonPath("$.genre").value(track.getGenre()))
                .andExpect(jsonPath("$.length").value(track.getLength()));
    }

    @Test
    public void createTrack_shouldReturn404IfArtistNotFound() throws Exception {
        var artistId = UUID.randomUUID();
        var createTrackRequest = new CreateTrackRequest("track1", "pop", 300);
        when(artistService.addNewTrack(artistId, createTrackRequest)).thenThrow(new ArtistNotFoundException(artistId));

        mockMvc.perform(post("/artists/{artistId}/tracks", artistId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createTrackRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createTrack_shouldReturn422IfTrackIsTooShort() throws Exception {
        var artistId = UUID.randomUUID();
        var createTrackRequest = new CreateTrackRequest("track1", "pop", 4);
        when(artistService.addNewTrack(artistId, createTrackRequest)).thenThrow(new TrackIsTooShortException());

        mockMvc.perform(post("/artists/{artistId}/tracks", artistId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createTrackRequest)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void addAlias_shouldCreateNewAliasForTheArtist() throws Exception {
        var artist = TestData.artist();
        var alias = TestData.artistAlias(artist.getId());
        artist.getAliases().add(alias);
        var addArtistAliasRequest = new AddArtistAliasRequest(alias.getName());
        when(artistService.addAlias(artist.getId(), addArtistAliasRequest)).thenReturn(artist);

        mockMvc.perform(post("/artists/{artistId}/aliases", artist.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(addArtistAliasRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(artist.getName()))
                .andExpect(jsonPath("$.aliases").isArray())
                .andExpect(jsonPath("$.aliases.length()").value(1))
                .andExpect(jsonPath("$.aliases[0]").value(addArtistAliasRequest.alias()));
    }

    @Test
    public void addAlias_shouldReturn404IfArtistNotFound() throws Exception {
        var artistId = UUID.randomUUID();
        var addArtistAliasRequest = new AddArtistAliasRequest("alias1");
        when(artistService.addAlias(artistId, addArtistAliasRequest)).thenThrow(new ArtistNotFoundException(artistId));

        mockMvc.perform(post("/artists/{artistId}/aliases", artistId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(addArtistAliasRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addAlias_shouldReturn409IfAliasAlreadyExists() throws Exception {
        var artist = TestData.artist();
        var alias = TestData.artistAlias(artist.getId());
        artist.getAliases().add(alias);
        var addArtistAliasRequest = new AddArtistAliasRequest(alias.getName());
        when(artistService.addAlias(artist.getId(), addArtistAliasRequest)).thenThrow(new AliasAlreadyExistsException(alias.getName(), artist.getName()));

        mockMvc.perform(post("/artists/{artistId}/aliases", artist.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(addArtistAliasRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    public void oftheday_shouldReturnArtist() throws Exception {
        var artist = TestData.artist();
        when(artistService.getArtistOfTheDay(LocalDate.now())).thenReturn(artist);

        mockMvc.perform(get("/artists/oftheday"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(artist.getName()));
    }

    private String asJsonString(Object any) {
        var objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(any);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
