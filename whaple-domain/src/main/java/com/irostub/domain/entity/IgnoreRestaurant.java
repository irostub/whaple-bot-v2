package com.irostub.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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
