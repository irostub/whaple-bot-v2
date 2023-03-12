package com.irostub.whaple.bot.weather;

import com.irostub.whaple.bot.DefaultBotCommand;
import com.irostub.whaple.bot.thirdparty.gps.kakao.GeoCache;
import com.irostub.whaple.bot.thirdparty.gps.kakao.GeoResponse;
import com.irostub.whaple.bot.thirdparty.weather.publicapi.weather.Category;
import com.irostub.whaple.bot.thirdparty.weather.publicapi.weather.ConvertGpsAndGrid;
import com.irostub.whaple.bot.thirdparty.weather.publicapi.weather.FixedShortTermWeatherData;
import com.irostub.whaple.bot.thirdparty.weather.publicapi.weather.PublicApiWeatherService;
import com.irostub.whaple.bot.thirdparty.weather.publicapi.weather.XYLatLng;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;

@Slf4j
@Component
public class WeatherCommand extends DefaultBotCommand {
    private final PublicApiWeatherService weatherService;
    private final ConvertGpsAndGrid convertGpsAndGrid;
    private final GeoCache geoCache;

    public WeatherCommand(PublicApiWeatherService weatherService,
                          ConvertGpsAndGrid convertGpsAndGrid,
                          GeoCache geoCache) {
        super("날씨", "날씨 정보를 표시합니다. {i}날씨 [지역] 을 사용하여 지역의 날씨를 조회할 수 있습니다.");
        this.weatherService = weatherService;
        this.convertGpsAndGrid = convertGpsAndGrid;
        this.geoCache = geoCache;
    }

    @Override
    public boolean isValid(AbsSender absSender, Message message, String[] arguments) {
        if (arguments.length == 1 && StringUtils.isNotEmpty(arguments[0])) {
            return true;
        }

        SendMessage send = SendMessage.builder()
                .chatId(message.getChatId())
                .text("날씨 명령어는 [지역] 을 입력해야합니다.")
                .build();
        try {
            absSender.execute(send);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, Integer messageId, String[] arguments) {
        GeoResponse geo = geoCache.getGeoResponse(arguments[0]);

        if (!hasGeoResult(absSender, chat.getId().toString(), geo)) return;

        String addressName = geo.getDocuments().get(0).getAddressName();
        Double x_gps = geo.getDocuments().get(0).getX();
        Double y_gps = geo.getDocuments().get(0).getY();

        XYLatLng xyLatLng = convertGpsAndGrid.convertGRID_GPS(ConvertGpsAndGrid.TO_GRID, y_gps, x_gps);

        Map<Category, FixedShortTermWeatherData> fixedShortTermWeatherDataMap = weatherService
                .requestWithGrid(xyLatLng.getXString(), xyLatLng.getYString());

        SendMessage sendMessage = WeatherMessageDirector.weatherMessage(chat.getId().toString(), fixedShortTermWeatherDataMap, addressName);
        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private boolean hasGeoResult(AbsSender absSender, String chatId, GeoResponse geo) {
        if (geo.getDocuments() == null || geo.getDocuments().isEmpty()) {
            SendMessage build = SendMessage.builder()
                    .chatId(chatId)
                    .text("지역 주소를 찾을 수 없어요.")
                    .build();
            try {
                absSender.execute(build);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }
}
