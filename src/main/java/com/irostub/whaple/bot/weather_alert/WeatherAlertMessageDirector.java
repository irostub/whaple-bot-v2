package com.irostub.whaple.bot.weather_alert;

import com.irostub.whaple.bot.thirdparty.weather.publicapi.weather.VilageFcstData;
import com.irostub.whaple.bot.thirdparty.weather.publicapi.weather.VilageFcstResponse;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class WeatherAlertMessageDirector {
    public static SendMessage cannotParseTimeMessage(Long chatId) {
        return SendMessage.builder()
                .text("날씨 알림이 필요한 시간을 0930 과 같이 입력해주세요.")
                .chatId(chatId)
                .build();
    }

    public static SendMessage registerSuccessMessage(Long chatId) {
        return SendMessage.builder()
                .text("날씨 알림을 등록했습니다.")
                .chatId(chatId)
                .build();
    }

    public static SendMessage cannotParseId(Long chatId) {
        return SendMessage.builder()
                .text("Id 값이 유효하지 않습니다.")
                .chatId(chatId)
                .build();
    }

    public static SendMessage deleteSuccessMessage(Long chatId, LocalTime localTime, String location) {
        return SendMessage.builder()
                .text(localTime.toString() + " " + location + " " + "날씨 알림을 삭제했습니다.")
                .chatId(chatId)
                .build();
    }

    public static SendMessage invalidTimeOrderIndex(Long chatId) {
        return SendMessage.builder()
                .text("선택하신 번호를 다시 확인해주세요. 1보다 작을 수 없습니다.")
                .chatId(chatId)
                .build();
    }

    public static SendMessage weatherAlertMessage(VilageFcstResponse vilageFcstResponse, Long userChatId, String addressName) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yy.MM.dd");
        StringBuilder sb = new StringBuilder();
        sb.append("===== ").append(now.format(dateFormatter)).append(" 날씨 알림 =====\n\n")
                .append("최고 기온 :").append(vilageFcstResponse.getMaxTemp()).append("\n")
                .append("최저 기온 :").append(vilageFcstResponse.getMinTemp()).append("\n\n");

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        int limit = 1;
        for (Map.Entry<LocalDateTime, VilageFcstData> entry : vilageFcstResponse.getVilageFcstDataMap().entrySet()) {
            LocalDateTime date = entry.getKey();
            if(limit % 2 == 1 && limit < 24 && (date.isAfter(now) || date.isEqual(now)) && date.getDayOfYear() == now.getDayOfYear()) {
                VilageFcstData value = entry.getValue();
                sb.append("--- ").append(date.format(timeFormatter)).append(" ---\n")
                        .append(value.toString())
                        .append("\n");
            }
            limit++;
        }
        sb.append(addressName).append(" 에서 측정되었습니다.");
        return SendMessage.builder()
                .text(sb.toString())
                .chatId(userChatId)
                .build();
    }
}
