package com.irostub.domain.repository;

import com.irostub.domain.entity.standard.IgnoreRestaurant;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.irostub.domain.entity.standard.QAccount.account;
import static com.irostub.domain.entity.standard.QIgnoreRestaurant.ignoreRestaurant;
import static com.irostub.domain.entity.standard.QRestaurant.restaurant;

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
