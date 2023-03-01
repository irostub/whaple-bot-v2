package com.irostub.whaple.bot.restaurant;

import java.util.Arrays;
import java.util.List;

public enum RestaurantSubCommand {
    CREATE("등록", "ㄷㄹ", "ㄷ", "추가", "ㅊㄱ", "add", "create", "a", "c"),
    DELETE("삭제", "ㅅㅈ", "ㅅ", "제거", "ㅈㄱ", "ㅈ", "delete", "remove", "r", "d"),
    RECOMMEND("추천", "ㅊㅊ", "ㅊ", "뭐먹", "ㅁㅁ", "ㅇㅈㅁ", "recommend", "rec"),
    IGNORE("무시", "ㅁㅅ", "ㅁ", "ignore"),
    RESTORE("복구", "ㅂㄱ", "restore"),
    READ("목록", "ㅁㄹ", "리스트", "ㄹㅅㅌ", "종류", "ㅈㄹ", "read", "list", "l"),
    HELP("?", "도움말", "도움", "ㄷㅇ", "help", "h");

    private final List<String> properties;

    RestaurantSubCommand(String... properties) {
        this.properties = List.of(properties);
    }

    public static RestaurantSubCommand of(String restaurantSubCommand) {
        return Arrays.stream(RestaurantSubCommand.values())
                .parallel()
                .filter(s -> s.properties.contains(restaurantSubCommand))
                .findFirst()
                .orElse(HELP);
    }

    public List<String> getProperties() {
        return properties;
    }
}
