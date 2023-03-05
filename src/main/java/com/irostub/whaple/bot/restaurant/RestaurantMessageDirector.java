package com.irostub.whaple.bot.restaurant;

import com.irostub.whaple.bot.utils.MessageEntityType;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

import static com.irostub.whaple.bot.utils.ExtractUtils.extractUsername;
import static com.irostub.whaple.bot.utils.ExtractUtils.getChatId;

public class RestaurantMessageDirector {

    /*
        recommend
    */
    private static MessageEntity recommendMessageEntity(User user) {
        return MessageEntity.builder()
                .type(MessageEntityType.TEXT_MENTION)
                .offset(5)
                .length(extractUsername(user).length())
                .user(user)
                .build();
    }

    public static SendMessage recommendMessage(Chat chat, User user, String recommendList) {
        return SendMessage.builder()
                .chatId(getChatId(chat))
                .entities(List.of(recommendMessageEntity(user)))
                .text("=====" + extractUsername(user) + "님의 오늘의 메뉴 =====\n" + recommendList)
                .build();
    }

    /*
        create
    */
    private static MessageEntity createRestaurantMessageEntity(User user) {
        return MessageEntity.builder()
                .type(MessageEntityType.TEXT_MENTION)
                .offset(0)
                .length(extractUsername(user).length())
                .user(user)
                .build();
    }

    public static SendMessage createExistsRestaurantMessage(Chat chat, User user, String restaurantName) {
        return SendMessage.builder()
                .chatId(chat.getId())
                .entities(List.of(createRestaurantMessageEntity(user)))
                .text(extractUsername(user) + "님 " + restaurantName + "(은)는 이미 등록되어있습니다.")
                .build();
    }

    public static SendMessage createRestaurantMessage(Chat chat, User user, String restaurantName) {
        return SendMessage.builder()
                .chatId(getChatId(chat))
                .entities(List.of(createRestaurantMessageEntity(user)))
                .text(extractUsername(user) + "님 " + restaurantName + "(이)가 등록되었습니다.")
                .build();
    }

    /*
        delete
    */
    public static SendMessage deleteNotOwnerRestaurantMessage(Chat chat, String restaurantName) {
        return SendMessage.builder()
                .chatId(getChatId(chat))
                .text(restaurantName + "(은)는 삭제할 권한이 없습니다.")
                .build();
    }


    public static SendMessage deleteRestaurantMessage(Chat chat, String restaurantName) {
        return SendMessage.builder()
                .chatId(getChatId(chat))
                .text(restaurantName + "(이)가 삭제되었습니다.")
                .build();
    }

    /*
        ignore
    */
    public static SendMessage ignoreRestaurantExistMessage(Chat chat, String accountName, String restaurantName) {
        return SendMessage.builder()
                .chatId(getChatId(chat))
                .text(accountName + "님은 이미 " + restaurantName + "(을)를 무시하셨어요.\n무맥락 음식 혐오를 멈춰주세요 😣")
                .build();
    }

    public static SendMessage successIgnoreRestuarantMessage(Chat chat, String restaurantName) {
        return SendMessage.builder()
                .chatId(getChatId(chat))
                .text(restaurantName + "(이)가 무시되었어요.\n" + restaurantName + "(을)를 싫어하시나봐요? 🤔")
                .build();
    }

    /*
        restore
     */
    public static SendMessage notExistIgnoreRestaurantMessage(Chat chat, String restaurantName) {
        return SendMessage.builder()
                .chatId(getChatId(chat))
                .text(restaurantName + "(을)를 무시한 적 없어요.")
                .build();
    }

    public static SendMessage successRestoreIgnoreRestaurantMessage(Chat chat, String restaurantName) {
        return SendMessage.builder()
                .chatId(getChatId(chat))
                .text(restaurantName + "(을)를 복구했어요.\n다시 추천 리스트에서 " + restaurantName + "(을)를 볼 수 있을 거예요!\uD83D\uDE0B")
                .build();
    }

    /*
        read
    */
    private static MessageEntity readRestaurantListMessageEntity(User user){
        return MessageEntity.builder()
                .type(MessageEntityType.TEXT_MENTION)
                .offset(5)
                .length(extractUsername(user).length())
                .user(user)
                .build();
    }

    public static SendMessage readRestaurantListMessage(Chat chat, User user, String restaurantNameList){
        return SendMessage.builder()
                .chatId(getChatId(chat))
                .entities(List.of(readRestaurantListMessageEntity(user)))
                .text("=====" + extractUsername(user) + "님 주문하신 밥집 목록 =====\n" + restaurantNameList)
                .build();
    }
}

