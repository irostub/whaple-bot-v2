package com.irostub.whaple.bot.restaurant;

import java.util.List;

public interface RestaurantRepositorySupport {
    List<Restaurant> findByRandom(Integer limit);
}
