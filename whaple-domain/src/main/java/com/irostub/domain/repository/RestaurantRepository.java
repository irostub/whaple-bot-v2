package com.irostub.domain.repository;

import com.irostub.domain.entity.standard.Restaurant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>, RestaurantRepositorySupport {

    @EntityGraph(attributePaths = {"account"})
    List<Restaurant> findAllBy();
    @EntityGraph(attributePaths = {"account"})
    Optional<Restaurant> findByName(String name);

    Boolean existsByName(String name);
}
