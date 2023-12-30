package com.irostub.standard.bot.rps;

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
public class RpsCommand extends DefaultBotCommand implements IManCommand {
    public RpsCommand() {
        super("가위바위보", "가위 바위 보를 할 수 있습니다.");
    }

    private final static String[] RPS = {"가위", "바위","보"};

    @Override
    public String getExtendedDescription() {
        return "!가위바위보 [가위, 바위, 보]\n" +
                "---예시---\n" +
                "!가위바위보 바위";
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, Integer messageId, String[] arguments) {
        if(arguments.length != 1){
            SendMessage message = SendMessage.builder()
                    .chatId(chat.getId())
                    .text(getExtendedDescription())
                    .build();
            sendMessage(absSender, message);
            return;
        }

        int random = RandomUtils.nextInt(0, 3);
        String select = RPS[random];

        SendMessage message = SendMessage.builder()
                .chatId(chat.getId())
                .text(select)
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
