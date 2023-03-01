package com.irostub.whaple.bot;

import com.irostub.whaple.bot.config.AppProperties;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

//아직은 웹훅 봇을 사용할 이유 X
public class WhapleWebhookBot extends TelegramWebhookBot{
    private final CommandHolder commandHolder;
    private final AppProperties properties;

    public WhapleWebhookBot(CommandHolder commandHolder, AppProperties appProperties) {
        super(appProperties.getBot().getToken());
        this.commandHolder = commandHolder;
        this.properties = appProperties;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (isSupportMessageType(message) && !filter(message)) {
                if (!commandHolder.executeCommand(this, message)) {
                    processInvalidCommandUpdate(update);
                }
                return null;
            }
        }
        processNonCommandUpdate(update);
        return null;
    }

    @Override
    public String getBotPath() {
        return null;
    }

    private boolean filter(Message message) {
        return false;
    }

    private boolean isSupportMessageType(Message message){
        return message.isUserMessage() || message.isGroupMessage() || message.isSuperGroupMessage();
    }

    private void processInvalidCommandUpdate(Update update) {
        processNonCommandUpdate(update);
    }

    private void processNonCommandUpdate(Update update) {

    }

    @Override
    public String getBotUsername() {
        return properties.getBot().getBotUsername();
    }
}
