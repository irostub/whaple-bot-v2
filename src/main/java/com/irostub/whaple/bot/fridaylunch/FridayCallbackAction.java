package com.irostub.whaple.bot.fridaylunch;

import com.irostub.whaple.bot.account.Account;
import com.irostub.whaple.bot.account.AccountRepository;
import com.irostub.whaple.bot.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
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
            if(chatId.equals(fridayLunchAccount.getFridayLunch().getChatGroup().getChatGroupId())){
                fridayLunchAccountRepository.delete(fridayLunchAccount);

                FridayLunch fridayLunch = fridayLunchRepository.findByChatGroupId(chatId).orElseThrow(NotFoundException::new);
                fridayLunch.getFridayLunchAccounts().remove(fridayLunchAccount);
                Message message = update.getCallbackQuery().getMessage();

                EditMessageText send = EditMessageText.builder()
                        .messageId(message.getMessageId())
                        .chatId(message.getChatId())
                        .text("금요일 점심 신청 인원 수 : " + fridayLunch.getFridayLunchAccounts().size())
                        .replyMarkup(message.getReplyMarkup())
                        .build();
                try {
                    absSender.execute(send);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
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

        EditMessageText send = EditMessageText.builder()
                .chatId(message.getChatId())
                .messageId(message.getMessageId())
                .replyMarkup(message.getReplyMarkup())
                .text("금요일 점심 신청 인원 수 : "+fridayLunch.getFridayLunchAccounts().size()+"\n"+
                        "신청자 : "+fridayLunch.getFridayLunchAccounts().stream().map(FridayLunchAccount::getAccount).map(Account::getName).collect(Collectors.joining(", ")))
                .build();
        try {
            absSender.execute(send);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
