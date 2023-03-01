package com.irostub.whaple.bot.restaurant;


import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.irostub.whaple.bot.account.QAccount.account;
import static com.irostub.whaple.bot.restaurant.QRestaurant.restaurant;

@RequiredArgsConstructor
@Repository
public class RestaurantRepositorySupportImpl implements RestaurantRepositorySupport {
    private final JPAQueryFactory query;

    @Override
    public List<Restaurant> findByRandom(Integer limit) {
        return query.selectFrom(restaurant)
                .leftJoin(restaurant.account, account)
                .fetchJoin()
                .orderBy(NumberExpression.random().desc())
                .limit(limit)
                .fetch();
    }
}
