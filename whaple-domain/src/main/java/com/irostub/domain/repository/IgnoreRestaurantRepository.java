package com.irostub.domain.repository;

import com.irostub.domain.entity.standard.Account;
import com.irostub.domain.entity.standard.IgnoreRestaurant;
import com.irostub.domain.entity.standard.Restaurant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IgnoreRestaurantRepository extends JpaRepository<IgnoreRestaurant, Long> {
    Optional<IgnoreRestaurant> findByAccountAndRestaurant(Account account, Restaurant restaurant);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    List<IgnoreRestaurant> findAllByAccount(Account account);

    List<IgnoreRestaurant> findByRestaurant(Restaurant restaurant);
}
