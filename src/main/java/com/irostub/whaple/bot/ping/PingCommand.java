package com.irostub.whaple.bot.ping;

import com.irostub.whaple.bot.DefaultBotCommand;
import com.irostub.whaple.bot.IManCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Instant;

@Slf4j
@Component
public class PingCommand extends DefaultBotCommand{
    public PingCommand() {
        super("핑", "서버 응답 딜레이 시간을 표시합니다. {i}핑 을 사용할 수 있습니다.");
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        Integer sendTime = message.getDate();
        long now = Instant.now().toEpochMilli();
        long delay = now - sendTime.longValue() * 1000;
        super.processMessage(absSender, message, new String[]{String.valueOf(delay)});
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, Integer messageId, String[] arguments) {
        SendMessage send = SendMessage.builder()
                .chatId(chat.getId())
                .text(arguments[0]+" ms")
                .build();
        try {
            absSender.execute(send);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
