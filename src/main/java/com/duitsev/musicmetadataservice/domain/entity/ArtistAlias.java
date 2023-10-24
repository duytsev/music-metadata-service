package com.duitsev.musicmetadataservice.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "artist_alias")
@Data
@NoArgsConstructor
public class ArtistAlias {

    @Id
    private UUID id;

    private String name;

    @Column(name = "artist_id")
    private UUID artistId;
}
