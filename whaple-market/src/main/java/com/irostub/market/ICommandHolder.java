package com.irostub.market;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;

public interface ICommandHolder {
    void registerDefaultAction(BiConsumer<AbsSender, Message> defaultConsumer);
    boolean register(IBotCommand botCommand);
    Map<IBotCommand, Boolean> registerAll(IBotCommand... botCommands);
    boolean deregister(IBotCommand botCommand);
    Map<IBotCommand, Boolean> deregisterAll(IBotCommand... botCommands);
    Collection<IBotCommand> getRegisteredCommands();
    IBotCommand getRegisteredCommand(String commandIdentifier);
}
