package com.irostub.standard.bot.checkio;

import com.irostub.standard.bot.DefaultBotCommand;
import com.irostub.standard.bot.IManCommand;
import com.irostub.domain.entity.standard.Account;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class CheckIoCommand extends DefaultBotCommand implements IManCommand {
    private final CheckIoService checkIoService;

    public CheckIoCommand(CheckIoService checkIoService) {
        super("퇴근체크", "평일 퇴근을 잊지 않았는지 상기시켜줍니다. 봇과의 [개인 채팅]에서 {i}퇴근체크 를 사용해 기능을 켜고 끌 수 있습니다.");
        this.checkIoService = checkIoService;
    }

    @Override
    public String getExtendedDescription() {
        return "봇과의 개인 채팅에서만 활성화 할 수 있는 명령어입니다.\n"+
                "\n"+
                "퇴근 체크 기능이 활성화 되면 법정공휴일과 주말을 제외한 날에 봇으로부터 알림메시지를 받게됩니다. " +
                "알림은 퇴근 메시지 보내는 것을 잊지않았는지에 대한 상기목적으로 전송되며, 퇴근 메시지 전송여부와 무관하게 전송됩니다.\n" +
                "알림은 퇴근 마지노선인 오후 8시에 개인 메시지로 받아볼 수 있습니다.";
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, Integer messageId, String[] arguments) {
        if(!chat.isUserChat()){
            SendMessage send = SendMessage.builder()
                    .text("퇴근 체크 명령어는 봇과의 개인 대화에서만 활성화 시킬 수 있습니다.")
                    .chatId(chat.getId())
                    .build();
            try {
                absSender.execute(send);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        Account account = checkIoService.switchCheckIoService(chat.getId());
        SendMessage send;
        if(account.getCheckIo()){
            send = SendMessage.builder()
                    .text(account.getName() + "님의 퇴근 체크 기능이 [활성화] 되었습니다.")
                    .chatId(chat.getId())
                    .build();
        }else{
            send = SendMessage.builder()
                    .text(account.getName() + "님의 퇴근 체크 기능이 [비활성화] 되었습니다.")
                    .chatId(chat.getId())
                    .build();
        }
        try {
            absSender.execute(send);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
