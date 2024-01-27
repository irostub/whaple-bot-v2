package com.irostub.standard.bot;

import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public abstract class BotCommand implements IBotCommand{
    public final static String COMMAND_INIT_CHARACTER = "!";
    public final static String COMMAND_PARAMETER_SEPARATOR = " ";
    public final static String DESCRIPTION_SWAP_IDENTIFIER = "{i}";
    private final static int COMMAND_MAX_LENGTH = 4096;

    private final String commandIdentifier;
    private final String description;

    public BotCommand(String commandIdentifier, String description) {

        if (commandIdentifier == null || commandIdentifier.isEmpty()) {
            throw new IllegalArgumentException("commandIdentifier for command cannot be null or empty");
        }

        if (commandIdentifier.startsWith(COMMAND_INIT_CHARACTER)) {
            commandIdentifier = commandIdentifier.substring(1);
        }

        if (commandIdentifier.length() + 1 > COMMAND_MAX_LENGTH) {
            throw new IllegalArgumentException("commandIdentifier cannot be longer than " + COMMAND_MAX_LENGTH + " (including " + COMMAND_INIT_CHARACTER + ")");
        }

        this.commandIdentifier = commandIdentifier.toLowerCase();
        swapIdentifier(description);
        this.description = swapIdentifier(description);
    }

    private String swapIdentifier(String description) {
        return StringUtils.replace(description, DESCRIPTION_SWAP_IDENTIFIER, COMMAND_INIT_CHARACTER);
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        execute(absSender, message.getFrom(), message.getChat(), arguments);
    }
    public abstract void execute(AbsSender absSender, User user, Chat chat, String[] arguments);

    @Override
    public final String getCommandIdentifier() {
        return commandIdentifier;
    }
    @Override
    public final String getDescription() {
        return description;
    }
    @Override
    public String toString() {
        return "<b>" + COMMAND_INIT_CHARACTER + getCommandIdentifier() +
                "</b>\n" + getDescription();
    }

    @Override
    public boolean isPublicCommand() {
        return true;
    }
}
