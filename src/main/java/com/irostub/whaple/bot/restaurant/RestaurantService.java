package com.irostub.whaple.bot.restaurant;

import com.irostub.whaple.bot.account.Account;
import com.irostub.whaple.bot.account.AccountRepository;
import com.irostub.whaple.bot.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.irostub.whaple.bot.restaurant.RestaurantMessageDirector.createExistsRestaurantMessage;
import static com.irostub.whaple.bot.restaurant.RestaurantMessageDirector.createRestaurantMessage;
import static com.irostub.whaple.bot.restaurant.RestaurantMessageDirector.deleteNotOwnerRestaurantMessage;
import static com.irostub.whaple.bot.restaurant.RestaurantMessageDirector.deleteRestaurantMessage;
import static com.irostub.whaple.bot.restaurant.RestaurantMessageDirector.ignoreRestaurantExistMessage;
import static com.irostub.whaple.bot.restaurant.RestaurantMessageDirector.notExistIgnoreRestaurantMessage;
import static com.irostub.whaple.bot.restaurant.RestaurantMessageDirector.successIgnoreRestuarantMessage;
import static com.irostub.whaple.bot.restaurant.RestaurantMessageDirector.successRestoreIgnoreRestaurantMessage;

@RequiredArgsConstructor
@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final IgnoreRestaurantRepository ignoreRestaurantRepository;
    private final IgnoreRestaurantQueryRepository ignoreRestaurantQueryRepository;
    private final AccountRepository accountRepository;

    public List<Restaurant> getAllRestaurant() {
        return restaurantRepository.findAllBy();
    }

    public List<Restaurant> recommend(Long accountId, int limit) {

        List<Restaurant> randoms = restaurantRepository.findByRandom(limit);
        List<Restaurant> restaurants = removeIgnoreRestaurant(accountId, randoms);
        if (randoms.isEmpty()) {
            return null;
        }
        if(restaurants.isEmpty()){
            recommend(accountId, limit);
        }
        return restaurants;
    }

    @Transactional
    public void add(AbsSender absSender, Chat chat, User user, Long accountId, String option) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findByName(option);
        if (optionalRestaurant.isPresent()) {
            SendMessage send = createExistsRestaurantMessage(chat, user, option);
            sendMessage(absSender, send);
            return;
        }

        Account account = accountRepository.findByAccountId(accountId).orElseThrow(NotFoundException::new);
        Restaurant restaurant = Restaurant.newRestaurant(option, null, null, account);
        restaurantRepository.save(restaurant);
        SendMessage sendMessage = createRestaurantMessage(chat, user, restaurant.getName());
        sendMessage(absSender, sendMessage);
    }

    public boolean isOwner(Long userId, String restaurantName){
        Restaurant restaurant = restaurantRepository.findByName(restaurantName).orElseThrow(NotFoundException::new);
        return restaurant.getCreatedBy().equals(userId.toString());
    }

    public void delete(AbsSender absSender, Chat chat, Long userId, String restaurantName) {
        if (!isOwner(userId, restaurantName)) {
            SendMessage send = deleteNotOwnerRestaurantMessage(chat, restaurantName);
            sendMessage(absSender, send);
            return;
        }
        Restaurant restaurant = restaurantRepository.findByName(restaurantName).orElseThrow(NotFoundException::new);
        List<IgnoreRestaurant> ignoreRestaurants = ignoreRestaurantRepository.findByRestaurant(restaurant);
        ignoreRestaurantRepository.deleteAll(ignoreRestaurants);
        restaurantRepository.delete(restaurant);
        SendMessage send = deleteRestaurantMessage(chat, restaurantName);
        sendMessage(absSender, send);
    }

    @Transactional
    public void ignore(AbsSender absSender, Chat chat, Long userId, String restaurantName) {
        Account account = accountRepository.findByAccountId(userId)
                .orElseThrow(NotFoundException::new);

        Restaurant restaurant = restaurantRepository.findByName(restaurantName)
                .orElseThrow(NotFoundException::new);

        Optional<IgnoreRestaurant> exist = ignoreRestaurantRepository.findByAccountAndRestaurant(account, restaurant);
        SendMessage message;

        if (exist.isPresent()) {
            message = ignoreRestaurantExistMessage(chat, account.getName(), restaurantName);
        } else {
            IgnoreRestaurant ignoreRestaurant = new IgnoreRestaurant(account, restaurant);
            ignoreRestaurantRepository.save(ignoreRestaurant);
            message = successIgnoreRestuarantMessage(chat, restaurantName);
        }
        sendMessage(absSender, message);
    }

    @Transactional
    public void restore(AbsSender absSender, Chat chat, Long userId, String restaurantName) {

        Account account = accountRepository.findByAccountId(userId)
                .orElseThrow(NotFoundException::new);

        Restaurant restaurant = restaurantRepository.findByName(restaurantName)
                .orElseThrow(NotFoundException::new);


        Optional<IgnoreRestaurant> ignoreRestaurant = ignoreRestaurantRepository.findByAccountAndRestaurant(account, restaurant);
        SendMessage message;
        if (ignoreRestaurant.isPresent()) {
            ignoreRestaurantRepository.delete(ignoreRestaurant.get());
            message = successRestoreIgnoreRestaurantMessage(chat, restaurantName);
        } else {
            message = notExistIgnoreRestaurantMessage(chat, restaurantName);
        }
        sendMessage(absSender, message);
    }

    private List<Restaurant> removeIgnoreRestaurant(Long accountId, List<Restaurant> list) {
        List<Long> ignoreList = ignoreRestaurantQueryRepository.getIgnoreList(accountId).stream()
                .map(IgnoreRestaurant::getRestaurant)
                .map(Restaurant::getId).collect(Collectors.toList());
        List<Restaurant> result = list.stream().filter(attr -> !ignoreList.contains(attr.getId())).collect(Collectors.toList());
        return result;
    }

    private void sendMessage(AbsSender absSender, SendMessage send) {
        try {
            absSender.execute(send);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
