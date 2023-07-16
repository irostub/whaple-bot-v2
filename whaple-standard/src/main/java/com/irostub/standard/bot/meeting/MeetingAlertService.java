package com.irostub.standard.bot.meeting;

import com.irostub.standard.bot.config.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class MeetingAlertService {
    private final AppProperties appProperties;
    private final AbsSender absSender;

    private final Set<LocalDate> holidays = Set.of(
            LocalDate.of(2023, 8, 15),
            LocalDate.of(2023, 9, 28),
            LocalDate.of(2023, 9, 29),
            LocalDate.of(2023, 9, 30),
            LocalDate.of(2023, 10, 3),
            LocalDate.of(2023, 10, 9),
            LocalDate.of(2023, 12, 25)
    );

    @Scheduled(cron = "0 55 10 * * 1-5", zone="Asia/Seoul")
    public void meetingAlert(){
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        if (holidays.contains(now)) {
            return;
        }

        SendMessage send = SendMessage.builder()
                .text("[알림] 5분 후, 11시 스크럼 회의가 시작됩니다!")
                .chatId(appProperties.getScrumId())
                .build();
        try {
            absSender.execute(send);
        } catch (TelegramApiException e) {
            log.error("fail to meeting message send, error", e);
            throw new RuntimeException(e);
        }
    }
}
