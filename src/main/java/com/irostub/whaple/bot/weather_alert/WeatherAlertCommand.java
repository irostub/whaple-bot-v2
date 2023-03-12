package com.irostub.whaple.bot.weather_alert;

import com.irostub.whaple.bot.DefaultBotCommand;
import com.irostub.whaple.bot.IManCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class WeatherAlertCommand extends DefaultBotCommand implements IManCommand {
    private final WeatherAlertService weatherAlertService;
    public WeatherAlertCommand(WeatherAlertService weatherAlertService) {
        super("예보", "평일에 [지정한 시간과 지정한 위치]의 날씨 예보를 개인 봇 메시지로 받아볼 수 있습니다. 봇과의 [개인 채팅]에서 {i}예보 를 사용할 수 있습니다. {i}도움말 예보 을 사용하여 사용방법을 알아보세요.");
        this.weatherAlertService = weatherAlertService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, Integer messageId, String[] arguments) {
        if (validateUserMessage(absSender, chat)) return;
        if (validateArgument(absSender, chat, arguments)) return;
        switch(arguments[0]){
            case "등록":
                weatherAlertService.register(absSender, chat.getId(), user.getId(), arguments[1], arguments[2]);
                break;
            case "목록":
                weatherAlertService.listUp(absSender, chat.getId(), user.getId());
                break;
            case "삭제":
                weatherAlertService.delete(absSender, chat.getId(), user.getId(), arguments[1]);
                break;
            default:
                weatherAlertService.notSupport(absSender, chat.getId(), arguments[0]);
                break;
        }
    }

    private boolean validateArgument(AbsSender absSender, Chat chat, String[] arguments) {
        if (arguments.length == 0) {
            //인자 없으면 잘못 사용
            sendHelp(absSender, chat);
            return true;
        } else if (arguments.length == 1) {
            //목록은 인자가 1개
            String com1 = arguments[0];
            if(!"목록".equals(com1)){
                sendHelp(absSender, chat);
                return true;
            }
        } else if (arguments.length == 2) {
            //삭제는 인자가 2개
            String com1 = arguments[0];
            String com2 = arguments[1];
            if(!"삭제".equals(com1)){
                sendHelp(absSender, chat);
                return true;
            }
        } else if (arguments.length == 3) {
            //등록은 인자가 3개
            String com1 = arguments[0];
            String com2 = arguments[1];
            String com3 = arguments[2];
            if (!"등록".equals(com1)) {
                sendHelp(absSender, chat);
                return true;
            }
        }
        return false;
    }

    private void sendHelp(AbsSender absSender, Chat chat) {
        SendMessage send = SendMessage.builder()
                .parseMode("HTML")
                .text(toMan())
                .chatId(chat.getId())
                .build();
        execute(absSender, send);
    }

    private boolean validateUserMessage(AbsSender absSender, Chat chat) {
        if (!chat.isUserChat()) {
            SendMessage send = SendMessage.builder()
                    .text("예보 명령어는 봇과의 개인 대화에서만 활성화 시킬 수 있습니다.")
                    .chatId(chat.getId())
                    .build();
            execute(absSender, send);
            return true;
        }
        return false;
    }

    private void execute(AbsSender absSender, SendMessage send) {
        try {
            absSender.execute(send);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getExtendedDescription() {
        return
                "!예보 [명령] [옵션1] [옵션2]\n" +
                        "[명령] 에 들어갈 수 있는 명령은 [등록, 목록, 삭제] 가 있습니다.\n" +
                        "[옵션1] 은 [날씨 측정 주소지] 를 넣을 수 있으며 [등록] 명령에서만 사용할 수 있습니다.\n" +
                        "[옵션1] 은 [목록으로 조회 한 예보 ID] 를 넣을 수 있으며 [삭제] 명령에서만 사용할 수 있습니다.\n" +
                        "[옵션2] 은 날씨 예보 받을 시간을 지정합니다. 0930 과 같은 형식으로 지정합니다. [등록] 명령 사용시 필수로 필요합니다.\n"+
                        "\n" +
                        "---예시---\n" +
                        "!예보 등록 와탭랩스 0800 - 날씨 예보를 등록합니다. 8시에 와탭랩스 위치의 날씨 예보를 받습니다.\n" +
                        "!예보 목록 - 날씨 예보을 등록한 목록을 보여줍니다. [ID] 가 함께 보이며 해당 ID 를 삭제 시 사용할 수 있습니다.\n" +
                        "!예보 삭제 1 - 등록된 날씨 예보 1번을 삭제합니다. ID 1번은 예시이며 !예보 목록으로 확인할 수 있습니다.\n";
    }
}
