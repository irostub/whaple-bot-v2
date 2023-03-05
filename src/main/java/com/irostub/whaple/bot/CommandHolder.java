package com.irostub.whaple.bot;



import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class CommandHolder implements ICommandHolder{
    private final Map<String, IBotCommand> commandHolderMap = new HashMap<>();
    private BiConsumer<AbsSender, Message> defaultConsumer;
    public CommandHolder() {
    }

    public final boolean executeCommand(AbsSender absSender, Message message) {
        if (message.hasText()) {
            String text = message.getText();
            if (text.startsWith(BotCommand.COMMAND_INIT_CHARACTER)) {
                String commandMessage = text.substring(1);
                String[] commandSplit = StringUtils.split(commandMessage, BotCommand.COMMAND_PARAMETER_SEPARATOR);

                String command = commandSplit[0];
                if (commandHolderMap.containsKey(commandSplit[0])) {
                    String[] parameters = Arrays.copyOfRange(commandSplit, 1, commandSplit.length);
                    commandHolderMap.get(command).processMessage(absSender, message, parameters);
                    return true;
                } else if (defaultConsumer != null) {
                    defaultConsumer.accept(absSender, message);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void registerDefaultAction(BiConsumer<AbsSender, Message> defaultConsumer) {
        this.defaultConsumer = defaultConsumer;
    }

    @Override
    public final boolean register(IBotCommand botCommand) {
        if (commandHolderMap.containsKey(botCommand.getCommandIdentifier())) {
            return false;
        }
        commandHolderMap.put(botCommand.getCommandIdentifier(), botCommand);
        return true;
    }

    @Override
    public final Map<IBotCommand, Boolean> registerAll(IBotCommand... botCommands) {
        Map<IBotCommand, Boolean> resultMap = new HashMap<>(botCommands.length);
        for (IBotCommand botCommand : botCommands) {
            resultMap.put(botCommand, register(botCommand));
        }
        return resultMap;
    }

    @Override
    public final boolean deregister(IBotCommand botCommand) {
        if (commandHolderMap.containsKey(botCommand.getCommandIdentifier())) {
            commandHolderMap.remove(botCommand.getCommandIdentifier());
            return true;
        }
        return false;
    }

    @Override
    public final Map<IBotCommand, Boolean> deregisterAll(IBotCommand... botCommands) {
        Map<IBotCommand, Boolean> resultMap = new HashMap<>(botCommands.length);
        for (IBotCommand botCommand : botCommands) {
            resultMap.put(botCommand, deregister(botCommand));
        }
        return resultMap;
    }

    @Override
    public final Collection<IBotCommand> getRegisteredCommands() {
        return commandHolderMap.values();
    }

    @Override
    public final IBotCommand getRegisteredCommand(String commandIdentifier) {
        return commandHolderMap.get(commandIdentifier);
    }

}
