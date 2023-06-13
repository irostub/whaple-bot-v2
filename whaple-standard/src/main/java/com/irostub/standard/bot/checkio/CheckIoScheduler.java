package com.irostub.standard.bot.checkio;

import com.irostub.domain.entity.Account;
import com.irostub.standard.bot.utils.HolidayUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Component
public class CheckIoScheduler {
    private final AbsSender absSender;
    private final CheckIoService checkIoService;
    @Scheduled(cron = "0 0 20 * * MON-FRI",zone="Asia/Seoul")
    public void checkIoSchedule(){
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        if (HolidayUtils.isHoliday(dtf.format(now))) {
            return;
        }

        List<Account> accounts = checkIoService.findScheduledAccount();
        for (Account account : accounts) {
            SendMessage send = SendMessage.builder()
                    .text("오늘 하루도 고생하셨습니다. 퇴근은 잘 하셨나요? \uD83D\uDE04")
                    .chatId(account.getUserChatId())
                    .build();
            try {
                absSender.execute(send);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
