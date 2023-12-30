package com.irostub.standard.bot.vs;

import com.irostub.standard.bot.DefaultBotCommand;
import com.irostub.standard.bot.IManCommand;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class VsCommand extends DefaultBotCommand implements IManCommand {
    public VsCommand() {
        super("vs", "짬뽕 vs 짜장");
    }

    @Override
    public String getExtendedDescription() {
        return "!vs 짬뽕 짜장 탕수육 사천탕수육";
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, Integer messageId, String[] arguments) {
        if(arguments.length < 1){
            SendMessage message = SendMessage.builder()
                    .chatId(chat.getId())
                    .text(getExtendedDescription())
                    .build();
            sendMessage(absSender, message);
            return;
        }

        int random = RandomUtils.nextInt(0, arguments.length);
        SendMessage message = SendMessage.builder()
                .chatId(chat.getId())
                .text(arguments[random])
                .build();
        sendMessage(absSender, message);
    }

    private void sendMessage(AbsSender absSender, SendMessage send) {
        try {
            absSender.execute(send);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
