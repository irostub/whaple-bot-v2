package com.irostub.domain.repository;


import com.irostub.domain.entity.standard.Restaurant;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.irostub.domain.entity.standard.QAccount.account;
import static com.irostub.domain.entity.standard.QRestaurant.restaurant;

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
