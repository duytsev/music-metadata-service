package com.duitsev.musicmetadataservice.repository;

import com.duitsev.musicmetadataservice.domain.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, UUID> {

    Artist findByIdx(long idx);

    Artist findFirstByOrderByIdAsc();
}
