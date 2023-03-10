package com.irostub.whaple.bot.fridaylunch;

import com.irostub.whaple.bot.DefaultBotCommand;
import com.irostub.whaple.bot.IManCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component
public class FridayLunchCommand extends DefaultBotCommand implements IManCommand {
    private final FridayLunchService fridayLunchService;

    public FridayLunchCommand(FridayLunchService fridayLunchRepository) {
        super("점심예약", "금요일 점심 인원수 체크 기능을 켜거나 끌 수 있습니다. {i}점심예약 을 사용할 수 있습니다.");
        this.fridayLunchService = fridayLunchRepository;
    }

    @Override
    public String getExtendedDescription() {
        return
                "점심 예약이 켜진 경우, 목요일 9시에 금요일 점심 인원 수를 확인하는 메세지가 예약을 켠 그룹 채팅방에 전송됩니다. 예약 마감은 11시 30분입니다.\n"+
                "점심 예약을 끄려면 {i}점심예약 을 한번 더 실행하세요.";
    }

    @Override
    public boolean isValid(AbsSender absSender, Message message, String[] arguments) {
        if(message.isGroupMessage() || message.isSuperGroupMessage()){
            return true;
        }

        SendMessage send = SendMessage.builder()
                .chatId(message.getChatId())
                .text("점심예약 명령어는 그룹 채팅방에서만 사용할 수 있습니다.")
                .build();
        try {
            absSender.execute(send);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, Integer messageId, String[] arguments) {
        FridayLunch fridayLunch = fridayLunchService.ChangeAlertState(chat);

        SendMessage send;
        if (fridayLunch.isAlertOn()) {
            send = SendMessage.builder()
                    .chatId(chat.getId())
                    .text("점심 예약 기능을 [켜짐] 상태로 전환했습니다.")
                    .build();
        }else{
            send = SendMessage.builder()
                    .chatId(chat.getId())
                    .text("점심 예약 기능을 [꺼짐] 상태로 전환했습니다.")
                    .build();
        }

        try {
            absSender.execute(send);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
