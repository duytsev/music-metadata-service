package com.duitsev.musicmetadataservice.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "track")
@Data
@NoArgsConstructor
public class Track {

    @Id
    private UUID id;

    private String title;

    private String genre;

    private int length;

    @Column(name = "artist_id")
    private UUID artistId;
}

