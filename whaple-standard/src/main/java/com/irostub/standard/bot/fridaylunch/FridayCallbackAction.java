package com.irostub.standard.bot.fridaylunch;

import com.irostub.domain.entity.standard.Account;
import com.irostub.domain.entity.standard.FridayLunch;
import com.irostub.domain.entity.standard.FridayLunchAccount;
import com.irostub.domain.repository.AccountRepository;
import com.irostub.domain.repository.FridayLunchAccountRepository;
import com.irostub.domain.repository.FridayLunchRepository;
import com.irostub.standard.bot.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class FridayCallbackAction {
    private final FridayLunchAccountRepository fridayLunchAccountRepository;
    private final FridayLunchRepository fridayLunchRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public void callback(AbsSender absSender, Update update){
        String callbackId = update.getCallbackQuery().getId();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Long userId = update.getCallbackQuery().getFrom().getId();

        Account account = accountRepository.findByAccountId(userId).orElseThrow(NotFoundException::new);
        List<FridayLunchAccount> fridayLunchAccounts = fridayLunchAccountRepository.findByAccount(account);

        if (fridayLunchAccounts.size() > 0) {
            FridayLunchAccount fridayLunchAccount = fridayLunchAccounts.get(0);
            //해당 채널에서 이미 점심예약을 한 경우
            if(chatId.equals(fridayLunchAccount.getFridayLunch().getChatGroup().getChatGroupId())){
                fridayLunchAccountRepository.delete(fridayLunchAccount);

                FridayLunch fridayLunch = fridayLunchRepository.findByChatGroupId(chatId).orElseThrow(NotFoundException::new);
                fridayLunch.getFridayLunchAccounts().remove(fridayLunchAccount);
                Message message = update.getCallbackQuery().getMessage();
                String enterUsernames = getEnterUsername(fridayLunch);
                EditMessageText send = EditMessageText.builder()
                        .messageId(message.getMessageId())
                        .chatId(message.getChatId())
                        .text("금요일 점심 신청 인원 수 : " + fridayLunch.getFridayLunchAccounts().size()+"\n"+
                                (StringUtils.isNotEmpty(enterUsernames)?"신청자 : "+enterUsernames:""))
                        .replyMarkup(message.getReplyMarkup())
                        .build();
                try {
                    absSender.execute(send);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                return;
            }

            //다른 채널에서 이미 점심예약을 한 경우
            AnswerCallbackQuery answer = AnswerCallbackQuery.builder()
                    .callbackQueryId(callbackId)
                    .text(fridayLunchAccount.getFridayLunch().getChatGroup().getChatGroupName() + "에서 이미 밥이 나오는 버튼을 눌렀습니다.")
                    .cacheTime(1000)
                    .build();
            try {
                absSender.execute(answer);
                return;
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        Optional<FridayLunch> fridayLunchOptional = fridayLunchRepository.findByChatGroupId(chatId);
        //점심예약 기능이 그룹 채팅에 켜져있지 않은 경우
        if (fridayLunchOptional.isEmpty()) {
            AnswerCallbackQuery answer = AnswerCallbackQuery.builder()
                    .callbackQueryId(callbackId)
                    .text("해당 채널에 점심 예약 기능이 활성화되어있지 않습니다.")
                    .cacheTime(1000)
                    .build();
            try {
                absSender.execute(answer);
                return;
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
        FridayLunch fridayLunch = fridayLunchOptional.get();
        FridayLunchAccount fridayLunchAccount = FridayLunchAccount.create(account, fridayLunch);
        fridayLunchAccountRepository.save(fridayLunchAccount);
        Message message = update.getCallbackQuery().getMessage();


        //점심 신청
        String enterUsernames = getEnterUsername(fridayLunch);
        EditMessageText send = EditMessageText.builder()
                .chatId(message.getChatId())
                .messageId(message.getMessageId())
                .replyMarkup(message.getReplyMarkup())
                .text("금요일 점심 신청 인원 수 : " + fridayLunch.getFridayLunchAccounts().size() + "\n" +
                        (StringUtils.isNotEmpty(enterUsernames) ? "신청자 : " + enterUsernames : ""))
                .build();
        try {
            absSender.execute(send);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getEnterUsername(FridayLunch fridayLunch) {
        String enterUsernames = fridayLunch.getFridayLunchAccounts().stream()
                .map(FridayLunchAccount::getAccount)
                .map(Account::getName)
                .collect(Collectors.joining(", "));
        return enterUsernames;
    }
}
