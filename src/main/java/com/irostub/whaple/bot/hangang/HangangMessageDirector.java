package com.irostub.whaple.bot.hangang;

import com.irostub.whaple.bot.thirdparty.weather.publicapi.hangang.RiverData;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.format.DateTimeFormatter;

public class HangangMessageDirector {
    public static SendMessage hangangMessage(String chatId, RiverData riverData) {
        String strDate = riverData.getMeasureDateTime()
                .format(DateTimeFormatter.ofPattern("MMdd HH:mm"));
        StringBuilder builder = new StringBuilder();
        builder
                .append("[")
                .append(strDate)
                .append("]")
                .append(" 한강 수온 ")
                .append(riverData.getTemperature())
                .append("°C");
        return SendMessage.builder()
                .chatId(chatId)
                .text(builder.toString())
                .build();
    }

    public static SendMessage inspectionMessage(String chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("[한강 수도공사 점검중]")
                .build();
    }
}
