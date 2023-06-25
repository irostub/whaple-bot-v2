package com.irostub.domain.repository;

import com.irostub.domain.entity.market.ResaleBoard;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResaleBoardRepository extends JpaRepository<ResaleBoard, Long>, ResaleBoardQueryRepository{
    @EntityGraph(attributePaths = {"images", "webAppUser"})
    Optional<ResaleBoard> findAllRelationById(Long postId);
}
