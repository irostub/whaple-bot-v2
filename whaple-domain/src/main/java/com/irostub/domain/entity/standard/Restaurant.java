package com.irostub.domain.entity.standard;

import com.irostub.domain.entity.BaseUserEntity;
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
public class Restaurant extends BaseUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String url;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    public static Restaurant newRestaurant(String name, String description, String url, Account account){
        Restaurant restaurant = new Restaurant();
        restaurant.name = name;
        restaurant.description = description;
        restaurant.url = url;
        restaurant.account = account;
        return restaurant;
    }
}
