package com.irostub.whaple.bot.restaurant;

import com.irostub.whaple.bot.account.Account;
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

    public List<IgnoreRestaurant> getIgnoreList(Account findAccount) {
        return query.selectFrom(ignoreRestaurant)
                .join(ignoreRestaurant.restaurant, restaurant)
                .fetchJoin()
                .join(restaurant.account, account)
                .fetchJoin()
                .where(ignoreRestaurant.account.accountId.eq(findAccount.getAccountId()))
                .fetch();
    }
}
