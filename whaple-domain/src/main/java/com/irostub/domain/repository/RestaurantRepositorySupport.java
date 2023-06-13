package com.irostub.domain.repository;

import com.irostub.domain.entity.Restaurant;

import java.util.List;

public interface RestaurantRepositorySupport {
    List<Restaurant> findByRandom(Integer limit);
}
