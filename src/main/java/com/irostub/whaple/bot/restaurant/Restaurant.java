package com.irostub.whaple.bot.restaurant;

import com.irostub.whaple.bot.account.Account;
import com.irostub.whaple.bot.persistence.BaseUserEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
//    @OneToMany(mappedBy = "restaurant")
//    private List<Food> foods = new ArrayList<>();

    public static Restaurant newRestaurant(String name, String description, String url, Account account){
        Restaurant restaurant = new Restaurant();
        restaurant.name = name;
        restaurant.description = description;
        restaurant.url = url;
        restaurant.account = account;
//        restaurant.addFood(food);
        return restaurant;
    }
//
//    public void addFood(Food... foods) {
//        this.foods.addAll(List.of(foods));
//        for (Food food : foods) {
//            food.setRestaurant(this);
//        }
//    }
}
