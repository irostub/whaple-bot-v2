package com.irostub.standard.bot;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

public interface IBotCommand {
    String getCommandIdentifier();
    String getDescription();

    boolean isPublicCommand();
    void processMessage(AbsSender absSender, Message message, String[] arguments);
}
