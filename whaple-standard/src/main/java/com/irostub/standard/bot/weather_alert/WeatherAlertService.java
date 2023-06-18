package com.irostub.standard.bot.weather_alert;

import com.irostub.domain.entity.standard.Account;
import com.irostub.domain.entity.standard.WeatherAlert;
import com.irostub.domain.repository.WeatherAlertRepository;
import com.irostub.standard.bot.account.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.irostub.standard.bot.weather_alert.WeatherAlertMessageDirector.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class WeatherAlertService {
    private final WeatherAlertRepository weatherAlertRepository;
    private final AccountService accountService;


    @Transactional
    public void register(AbsSender absSender, Long chatId, Long userId, String location, String time) {
        if(StringUtils.isBlank(location)){
            return;
        }
        if (StringUtils.isBlank(time)) {
            return;
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HHmm");
        if (validTimeFormat(absSender, chatId, time, dtf)) return;

        if(validTime(absSender, chatId, time, dtf)) return;

        LocalTime localTime = LocalTime.parse(time, dtf);
        Account account = accountService.findById(userId);
        WeatherAlert weatherAlert = WeatherAlert.create(localTime, location, account);
        weatherAlertRepository.save(weatherAlert);


        SendMessage sendMessage = registerSuccessMessage(chatId);
        sendMessage(absSender, sendMessage);
    }

    private boolean validTime(AbsSender absSender, Long chatId, String time, DateTimeFormatter dtf) {
        LocalTime parse = LocalTime.parse(time, dtf);
        boolean before = parse.isBefore(LocalTime.of(3, 0, 0));
        if (before) {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text("날씨 예보 시간은 오전 3시 이후만 지정할 수 있습니다.")
                    .build();
            sendMessage(absSender, sendMessage);
        }
        return before;
    }

    @Transactional
    public void listUp(AbsSender absSender, Long chatId, Long userId) {
        Account account = accountService.findById(userId);
        StringBuilder sb = new StringBuilder("===== 날씨 예보 목록 =====\n\n");
        List<WeatherAlert> weatherAlerts = weatherAlertRepository.findByAccount(account).stream()
                    .sorted(Comparator.comparing(WeatherAlert::getScheduledTime))
                    .collect(Collectors.toList());
        int index = 1;
        for (WeatherAlert weatherAlert : weatherAlerts) {
            sb.append("[").append(index++).append("] ")
                    .append(weatherAlert.getScheduledTime().toString()).append(" ")
                    .append(weatherAlert.getLocation()).append("\n");
        }

        SendMessage sendMessage = SendMessage.builder()
                .text(sb.toString())
                .chatId(chatId)
                .build();
        sendMessage(absSender, sendMessage);
    }

    @Transactional
    public void delete(AbsSender absSender, Long chatId, Long userId, String timeOrderIndex) {
        if (validateTimeOrderIndex(absSender, chatId, timeOrderIndex)) return;
        //timeOrderIndex 는 1부터 시작 (사용자에게 보여지는 index 순서)
        int parsedWeatherAlertId = Integer.parseInt(timeOrderIndex);
        Account account = accountService.findById(userId);

        List<WeatherAlert> weatherAlerts = weatherAlertRepository.findByAccount(account).stream()
                .sorted(Comparator.comparing(WeatherAlert::getScheduledTime))
                .collect(Collectors.toList());
        if (weatherAlerts.get(parsedWeatherAlertId - 1) == null) {
            log.error("그럴리가 없는데...");
            return;
        }

        WeatherAlert weatherAlert = weatherAlerts.get(parsedWeatherAlertId - 1);
        weatherAlertRepository.delete(weatherAlert);

        SendMessage sendMessage = deleteSuccessMessage(chatId, weatherAlert.getScheduledTime(), weatherAlert.getLocation());
        sendMessage(absSender, sendMessage);
    }

    private boolean validateTimeOrderIndex(AbsSender absSender, Long chatId, String timeOrderIndex) {
        try {
            int i = Integer.parseInt(timeOrderIndex);
            if (i < 1) {
                SendMessage sendMessage = WeatherAlertMessageDirector.invalidTimeOrderIndex(chatId);
                sendMessage(absSender, sendMessage);
                return true;
            }
        } catch (NumberFormatException e) {
            SendMessage sendMessage = cannotParseId(chatId);
            sendMessage(absSender, sendMessage);
            return true;
        }
        return false;
    }

    private boolean validTimeFormat(AbsSender absSender, Long chatId, String time, DateTimeFormatter dtf) {
        try {
            dtf.parse(time);
        } catch (DateTimeParseException e) {
            SendMessage sendMessage = cannotParseTimeMessage(chatId);
            sendMessage(absSender, sendMessage);
            return true;
        }
        return false;
    }

    private void sendMessage(AbsSender absSender, SendMessage sendMessage){
        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void notSupport(AbsSender absSender, Long chatId, String argument) {
        SendMessage sendMessage = SendMessage.builder()
                .text("[" + argument + "] 는 지원하지 않는 부가명령입니다. 예보 도움말이 필요하시면 !도움말 예보 을 사용할 수 있습니다.")
                .chatId(chatId)
                .build();
        sendMessage(absSender, sendMessage);
        return;
    }
}
