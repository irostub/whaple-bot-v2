package com.irostub.market;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public abstract class DefaultBotCommand extends BotCommand {
    public DefaultBotCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        if(!isValid(absSender, message, arguments)){
            return;
        }
        execute(absSender, message.getFrom(), message.getChat(), message.getMessageId(), arguments);
    }


    @Override
    public final void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {}

    private void sendValidFailMessage() {

    }

    public boolean isValid(AbsSender absSender, Message message, String[] arguments){
        return true;
    }

    public abstract void execute(AbsSender absSender, User user, Chat chat, Integer messageId, String[] arguments);
}
