package com.irostub.whaple.bot.fridaylunch;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class FridayInteractiveDirector {
    private static InlineKeyboardButton fridayButton(){
        return InlineKeyboardButton.builder()
                .callbackData("friday")
                .text("밥이 나오는 버튼")
                .build();
    }
    public static InlineKeyboardMarkup fridayKeyboard(){
        InlineKeyboardButton button = fridayButton();
        List<InlineKeyboardButton> buttons = List.of(button);
        return InlineKeyboardMarkup.builder().keyboard(List.of(buttons)).build();
    }
}
