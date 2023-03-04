package com.irostub.whaple.bot.hangang;

import com.irostub.whaple.bot.DefaultBotCommand;
import com.irostub.whaple.bot.thirdparty.weather.publicapi.hangang.PublicApiHangangService;
import com.irostub.whaple.bot.thirdparty.weather.publicapi.hangang.RiverData;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.irostub.whaple.bot.hangang.HangangMessageDirector.hangangMessage;
import static com.irostub.whaple.bot.hangang.HangangMessageDirector.inspectionMessage;

@Component
public class HangangCommand extends DefaultBotCommand {
    private final PublicApiHangangService publicApiHangangService;


    public HangangCommand(PublicApiHangangService publicApiHangangService) {
        super("한강", "현재 한강의 수온을 표시합니다. {i}한강 을 사용할 수 있습니다.");
        this.publicApiHangangService = publicApiHangangService;
    }

    //todo:redis cache 로 1시간 마다 캐시잡을 것
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, Integer messageId, String[] arguments) {
        RiverData riverData = publicApiHangangService.sendHangangRequest();

        SendMessage sendMessage = riverData.isInspection() ?
                inspectionMessage(chat.getId().toString()) : hangangMessage(chat.getId().toString(), riverData);

        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
