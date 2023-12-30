package com.irostub.domain.repository;


import com.irostub.domain.entity.standard.Music;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<Music, Long> {
    Boolean existsByVideoId(String videoId);
}
