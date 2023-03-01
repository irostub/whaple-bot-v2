package com.irostub.whaple.bot.restaurant;

import com.irostub.whaple.bot.account.Account;
import com.irostub.whaple.bot.persistence.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class IgnoreRestaurant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    public IgnoreRestaurant(Account account, Restaurant restaurant) {
        this.account = account;
        this.restaurant = restaurant;
    }
}
