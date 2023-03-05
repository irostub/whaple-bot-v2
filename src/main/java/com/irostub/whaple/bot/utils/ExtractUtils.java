package com.irostub.whaple.bot.utils;

import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

public class ExtractUtils {
    public static String getChatId(Chat chat) {
        return chat.getId().toString();
    }

    public static String extractUsername(User user) {
        return getLastName(user)+getFirstName(user);
    }

    private static String getLastName(User user) {
        String lastName = user.getLastName();
        if (StringUtils.isNotBlank(lastName)) {
            return lastName;
        }
        return "";
    }

    private static String getFirstName(User user) {
        String firstName = user.getFirstName();
        if (StringUtils.isNotBlank(firstName)) {
            return firstName;
        }
        return "";
    }
}
