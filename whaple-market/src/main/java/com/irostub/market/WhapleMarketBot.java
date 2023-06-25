package com.irostub.market;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
public class WhapleMarketBot extends TelegramLongPollingBot {
    @Value("${app.bot.bot-username}")
    private String name;

    public WhapleMarketBot(@Value("${app.bot.token}")String token,
                           @Value("${spring.profiles.active}") String profilesActive) {
        super(token + (StringUtils.isNotEmpty(profilesActive) &&
                profilesActive.equals("local") ? "/test" : ""));
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("update={}", update);
    }

    @Override
    public String getBotUsername() {
        return name;
    }
}
