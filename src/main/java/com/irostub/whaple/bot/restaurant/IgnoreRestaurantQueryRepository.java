package com.irostub.whaple.bot.restaurant;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.irostub.whaple.bot.account.QAccount.account;
import static com.irostub.whaple.bot.restaurant.QIgnoreRestaurant.ignoreRestaurant;
import static com.irostub.whaple.bot.restaurant.QRestaurant.restaurant;

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
