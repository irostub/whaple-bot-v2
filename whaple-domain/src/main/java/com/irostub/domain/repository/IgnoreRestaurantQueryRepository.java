package com.irostub.domain.repository;

import com.irostub.domain.entity.IgnoreRestaurant;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.irostub.domain.entity.QAccount.account;
import static com.irostub.domain.entity.QIgnoreRestaurant.ignoreRestaurant;
import static com.irostub.domain.entity.QRestaurant.restaurant;

@RequiredArgsConstructor
@Repository
public class IgnoreRestaurantQueryRepository {
    private final JPAQueryFactory query;

    public List<IgnoreRestaurant> getIgnoreList(Long accountId) {
        return query.selectFrom(ignoreRestaurant)
                .join(ignoreRestaurant.restaurant, restaurant)
                .join(restaurant.account, account)
                .where(ignoreRestaurant.account.accountId.eq(accountId))
                .fetch();
    }
}
