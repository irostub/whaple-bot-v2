package com.irostub.whaple.bot.restaurant;

import com.irostub.whaple.bot.DefaultBotCommand;
import com.irostub.whaple.bot.IManCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class RestaurantCommand extends DefaultBotCommand implements IManCommand {
    public RestaurantCommand() {
        super("밥", "(사용 불가|명령어 공사중)밥 메뉴의 추천, 등록 등을 실행할 수 있습니다. {i}밥 을 사용할 수 있습니다.\n{i}도움말 밥 으로 자세한 사용법을 알아보세요.");
    }

    @Override
    public String getExtendedDescription() {

        return
                "!밥 [명령] [부가 옵션]\n" +
                "[명령] 에 들어갈 수 있는 명령은 [추가, 삭제, 무시, 복구] 가 있습니다.\n" +
                "[부가 옵션] 에 밥집 이름을 넣을 수 있습니다.\n" +
                "[명령] 과 [부가옵션] 을 지정하지 않으면 밥집 한 곳을 추천합니다.\n" +
                "\n" +
                "---예시---\n" +
                "!밥 - 밥집 한 곳을 추천합니다\n" +
                "!밥 추가 마녀김밥 - 마녀김밥 밥집을 등록합니다\n" +
                "!밥 삭제 마녀김밥 - 마녀김밥 밥집을 삭제합니다\n" +
                "!밥 무시 마녀김밥 - 마녀김밥을 자신의 추천 목록에서 제외합니다\n" +
                "!밥 복구 마녀김밥 - 마녀김밥을 자신의 추천 목록에서 복구합니다\n";
    }
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, Integer messageId, String[] arguments) {

    }
}
