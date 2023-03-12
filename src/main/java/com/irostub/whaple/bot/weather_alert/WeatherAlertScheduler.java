package com.irostub.whaple.bot.weather_alert;

import com.irostub.whaple.bot.thirdparty.gps.kakao.GeoCache;
import com.irostub.whaple.bot.thirdparty.gps.kakao.GeoResponse;
import com.irostub.whaple.bot.thirdparty.weather.publicapi.weather.ConvertGpsAndGrid;
import com.irostub.whaple.bot.thirdparty.weather.publicapi.weather.PublicApiWeatherService;
import com.irostub.whaple.bot.thirdparty.weather.publicapi.weather.VilageFcstResponse;
import com.irostub.whaple.bot.thirdparty.weather.publicapi.weather.XYLatLng;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@RequiredArgsConstructor
@Component
public class WeatherAlertScheduler {
    private final ConvertGpsAndGrid convertGpsAndGrid;
    private final GeoCache geoCache;
    private final PublicApiWeatherService publicApiWeatherService;
    private final WeatherAlertRepository weatherAlertRepository;
    private final AbsSender absSender;

    @Scheduled(cron = "* 0/1 * * * MON-FRI", zone = "Asia/Seoul")
//    @Scheduled(cron = "0/20 * * * * *", zone = "Asia/Seoul")
    public void weatherSchedule(){
        LocalTime now = LocalTime.now(ZoneId.of("Asia/Seoul"));
        List<WeatherAlert> weatherAlerts = weatherAlertRepository.findByScheduledTimeEquals(now);
        for (WeatherAlert weatherAlert : weatherAlerts) {
            Long userChatId = weatherAlert.getAccount().getUserChatId();
            GeoResponse geo = geoCache.getGeoResponse(weatherAlert.getLocation());

            if (!hasGeoResult(absSender, userChatId.toString(), geo)) return;

            String addressName = geo.getDocuments().get(0).getAddressName();
            Double x_gps = geo.getDocuments().get(0).getX();
            Double y_gps = geo.getDocuments().get(0).getY();

            XYLatLng xyLatLng = convertGpsAndGrid.convertGRID_GPS(ConvertGpsAndGrid.TO_GRID, y_gps, x_gps);
            VilageFcstResponse vilageFcstResponse = publicApiWeatherService.requestShortTermWithGrid(xyLatLng.getXString(), xyLatLng.getYString());

            SendMessage sendMessage = WeatherAlertMessageDirector.weatherAlertMessage(vilageFcstResponse, userChatId, addressName);

            try {
                absSender.execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
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
