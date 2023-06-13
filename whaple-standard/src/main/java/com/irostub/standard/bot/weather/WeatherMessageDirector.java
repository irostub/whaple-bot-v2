package com.irostub.standard.bot.weather;

import com.irostub.standard.bot.thirdparty.weather.publicapi.weather.Category;
import com.irostub.standard.bot.thirdparty.weather.publicapi.weather.FixedShortTermWeatherData;
import com.irostub.standard.bot.thirdparty.weather.publicapi.weather.RainType;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Map;


public class WeatherMessageDirector {

    public static SendMessage weatherMessage(String chatId, Map<Category, FixedShortTermWeatherData> dataMap, String address) {
        FixedShortTermWeatherData temp = dataMap.get(Category.T1H);
        FixedShortTermWeatherData rain = dataMap.get(Category.RN1);

        //습도
        FixedShortTermWeatherData humidity = dataMap.get(Category.REH);
        FixedShortTermWeatherData rainType = dataMap.get(Category.PTY);
        FixedShortTermWeatherData windVector = dataMap.get(Category.VEC);
        FixedShortTermWeatherData wind = dataMap.get(Category.WSD);
        //TODO:기온별 옷차림 넣을 것

        String formatTemp = String.format("기온 : %s", temp.getObsrValue());
        String formatHumidity = String.format("습도 : %s", humidity.getObsrValue());
        String formatWind = String.format("풍속 : %s", wind.getObsrValue());
        String formatWindVector = String.format("풍향 : %s", windVector.getObsrValue());
        String formatRainType = String.format("기상 : %s", rainType.getObsrValue());
        String formatRain = null;
        if (!rainType.getObsrValue().equals(RainType.NONE.toNumericString())) {
            formatRain = String.format("강수 : %s", rain.getObsrValue());
        }

        StringBuilder sb = new StringBuilder();
        sb.append(formatTemp).append("\n")
                .append(formatHumidity).append("\n")
                .append(formatWind).append("\n")
                .append(formatWindVector).append("\n")
                .append(formatRainType).append("\n");
        if (formatRain != null) {
            sb.append(formatRain).append("\n");
        }
        sb.append("\n")
                .append(address).append("에서 측정했어요!");

        return SendMessage.builder()
                .chatId(chatId)
                .text(sb.toString())
                .build();
    }
}
