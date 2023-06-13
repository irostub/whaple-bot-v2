package com.irostub.standard.bot.fridaylunch;

import com.irostub.domain.entity.Account;
import com.irostub.domain.entity.FridayLunch;
import com.irostub.domain.entity.FridayLunchAccount;
import com.irostub.domain.repository.FridayLunchAccountRepository;
import com.irostub.domain.repository.FridayLunchRepository;
import com.irostub.domain.entity.FridaySendMessageQueue;
import com.irostub.domain.repository.FridaySendMessageQueueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class FridayLunchScheduler {
    private final FridayLunchRepository fridayLunchRepository;
    private final FridayLunchAccountRepository fridayLunchAccountRepository;
    private final FridaySendMessageQueueRepository queueRepository;
    private final AbsSender absSender;
    @Scheduled(cron = "0 0 9 ? * 4", zone="Asia/Seoul")
//    @Scheduled(cron = "10 * * * * *")
    @Transactional
    public void sendCheckMessage(){

        List<FridayLunch> fridayLunches = fridayLunchRepository.findByAlert(true);
        try {
            for (FridayLunch fridayLunch : fridayLunches) {
                Long chatGroupId = fridayLunch.getChatGroup().getChatGroupId();
                InlineKeyboardMarkup keyboard = FridayInteractiveDirector.fridayKeyboard();
                SendMessage send = SendMessage.builder()
                        .text("금요일 점심 신청 인원 수 : " + fridayLunch.getFridayLunchAccounts().size())
                        .replyMarkup(keyboard)
                        .chatId(chatGroupId)
                        .build();
                absSender.executeAsync(send).thenAccept(message -> {
                    FridaySendMessageQueue fridaySendMessageQueue = FridaySendMessageQueue.create(message.getChatId(), message.getMessageId());
                    queueRepository.saveAndFlush(fridaySendMessageQueue);
                });
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Scheduled(cron = "0 30 11 ? * 4", zone="Asia/Seoul")
//    @Scheduled(cron = "30 * * * * *")
    public void sendTotalMessage() {
        //기존 예약 버튼 메세지 제거
        List<FridaySendMessageQueue> all = queueRepository.findAll();
        for (FridaySendMessageQueue fridaySendMessageQueue : all) {
            DeleteMessage send = DeleteMessage.builder()
                    .messageId(fridaySendMessageQueue.getMessageId())
                    .chatId(fridaySendMessageQueue.getChatId())
                    .build();
            try {
                absSender.execute(send);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            } finally {
                queueRepository.delete(fridaySendMessageQueue);
            }
        }

        //마감 메세지 전송
        List<FridayLunch> fridayLunches = fridayLunchRepository.findByAlert(true);
        for (FridayLunch fridayLunch : fridayLunches) {
            List<FridayLunchAccount> fridayLunchAccounts = fridayLunch.getFridayLunchAccounts();
            String enterAccount = fridayLunchAccounts.stream()
                    .map(FridayLunchAccount::getAccount)
                    .map(Account::getName)
                    .collect(Collectors.joining(", "));
            SendMessage send = SendMessage.builder()
                    .chatId(fridayLunch.getChatGroup().getChatGroupId())
                    .text("---금요일 점심 신청 인원 마감---\n" +
                            "총 신청 인원 : " + fridayLunchAccounts.size() + "\n" +
                            "신청자 : " + enterAccount)
                    .build();
            try {
                absSender.execute(send);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        //신청자 초기화
        fridayLunchAccountRepository.deleteAll();
    }
}
