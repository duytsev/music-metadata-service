package com.duitsev.musicmetadataservice.repository;

import com.duitsev.musicmetadataservice.domain.entity.ArtistAlias;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ArtistAliasRepository extends JpaRepository<ArtistAlias, UUID> {

    List<ArtistAlias> findByArtistId(UUID artistId);

}
