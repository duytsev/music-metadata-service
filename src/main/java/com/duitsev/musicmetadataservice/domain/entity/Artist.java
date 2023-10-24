package com.duitsev.musicmetadataservice.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "artist")
@Data
@NoArgsConstructor
public class Artist {

    @Id
    private UUID id;

    private String name;

    private long idx;

    @OneToMany()
    @JoinColumn(name = "artist_id")
    private Set<Track> tracks = new HashSet<>();

    @OneToMany()
    @JoinColumn(name = "artist_id")
    private Set<ArtistAlias> aliases = new HashSet<>();
}
