package com.irostub.standard.bot.restaurant;

import com.irostub.domain.entity.Restaurant;
import com.irostub.standard.bot.DefaultBotCommand;
import com.irostub.standard.bot.IManCommand;
import com.irostub.standard.bot.config.AccountHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RestaurantCommand extends DefaultBotCommand implements IManCommand {
    private final RestaurantService restaurantService;

    public RestaurantCommand(RestaurantService restaurantService) {
        super("밥", "밥 메뉴의 추천, 등록 등을 실행할 수 있습니다. {i}밥 을 사용할 수 있습니다.\n{i}도움말 밥 으로 자세한 사용법을 알아보세요.");
        this.restaurantService = restaurantService;
    }

    @Override
    public String getExtendedDescription() {
        return
                "!밥 [명령] [부가 옵션]\n" +
                        "[명령] 에 들어갈 수 있는 명령은 [등록, 목록, 삭제, 무시, 복구] 가 있습니다.\n" +
                        "[부가 옵션] 에 밥집 이름을 넣을 수 있습니다.\n" +
                        "[명령] 과 [부가옵션] 을 지정하지 않으면 밥집 한 곳을 추천합니다.\n" +
                        "\n" +
                        "---예시---\n" +
                        "!밥 - 밥집 한 곳을 추천합니다\n" +
                        "!밥 3 - 밥집 세곳을 추천합니다\n" +
                        "!밥 목록 - 등록된 모든 밥집을 출력합니다\n" +
                        "!밥 등록 마녀김밥 - 마녀김밥 밥집을 등록합니다\n" +
                        "!밥 삭제 마녀김밥 - 마녀김밥 밥집을 삭제합니다\n" +
                        "!밥 무시 마녀김밥 - 마녀김밥을 자신의 추천 목록에서 제외합니다\n" +
                        "!밥 복구 마녀김밥 - 마녀김밥을 자신의 추천 목록에서 복구합니다\n";
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, Integer messageId, String[] arguments) {
        if (arguments.length == 0) {
            AccountHolder accountHolder = new AccountHolder();
            Long accountId = accountHolder.getUser().getAccountId();
            List<Restaurant> restaurants = restaurantService.recommend(accountId, 1);
            if (restaurants == null) {
                return;
            }
            Restaurant restaurant = restaurants.get(0);
            if (restaurant != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(restaurant.getName()).append(" - ")
                        .append(restaurant.getAccount().getName()).append("님이 추천해주신 밥집입니다. 🥳");
                SendMessage send = SendMessage.builder()
                        .text(sb.toString())
                        .chatId(chat.getId())
                        .build();
                sendMessage(absSender, send);
            }
            return;
        }

        if (arguments.length == 1) {
            if ("목록".equals(arguments[0])) {
                String tableItem = restaurantService.getAllRestaurant().stream()
                        .map(r -> "<tr><td>" + r.getName() + "</td><td>" + r.getAccount().getName() + "</td></tr>")
                        .collect(Collectors.joining());
                HtmlToImage imageGenerator = new HtmlToImage();
                imageGenerator.loadHtml(
                                "<html><head><style>body { font-family: 'NanumSquare', monospace, sans-serif; font-size:16;}</style></head>"+
                                "<body>" +
                                "<table cellpadding=\"10\">" +
                                "<tr bgcolor=\"ffe4c4\"><th>밥집 이름</th><th>등록한 사람</th></tr>"
                                + tableItem +
                                "</table>" +
                                "</body></html>");
                File dir = new File(System.getProperty("user.home")+"/.whaple/image/");
                if(!dir.exists()){
                    dir.mkdirs();
                }
                imageGenerator.saveAsImage(System.getProperty("user.home")+"/.whaple/image/restaurantImage.png");
                File file = new File(System.getProperty("user.home")+"/.whaple/image/restaurantImage.png");
                InputFile inputFile = new InputFile();
                inputFile.setMedia(file);
                SendPhoto send = SendPhoto.builder()
                        .photo(inputFile)
                        .chatId(chat.getId())
                        .build();
                sendMessage(absSender, send);
                return;
            }

            try {
                String argument = arguments[0];
                int recommendCount = Integer.parseInt(argument);
                List<Restaurant> restaurants = restaurantService.recommend(user.getId(), recommendCount);
                String flatList = restaurants.stream()
                        .map(restaurant -> restaurant.getName() + " - " +
                                restaurant.getAccount().getName() +
                                "님이 추천해주신 밥집입니다.\n" + (StringUtils.isNotBlank(restaurant.getUrl())?restaurant.getUrl():""))
                        .collect(Collectors.joining("\n"));
                SendMessage send = SendMessage.builder()
                        .text(flatList)
                        .chatId(chat.getId())
                        .build();
                sendMessage(absSender, send);
            } catch (NumberFormatException e) {
                return;
            }
            return;
        }

        if (arguments.length > 1) {
            String subCommand = arguments[0];
            String option = StringUtils.join(arguments, " ", 1, arguments.length).strip();
            if (validOption(option)) {
                return;
            }

            switch (subCommand) {
                case "등록":
                    restaurantService.add(absSender, chat, user, user.getId(), option);
                    break;
                case "삭제":
                    restaurantService.delete(absSender, chat, user.getId(), option);
                    break;
                case "무시":
                    restaurantService.ignore(absSender, chat, user.getId(), option);
                    break;
                case "복구":
                    restaurantService.restore(absSender, chat, user.getId(), option);
                    break;
                default:
                    return;
            }
        }
    }

    private void sendMessage(AbsSender absSender, SendMessage send) {
        try {
            absSender.execute(send);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessage(AbsSender absSender, SendPhoto send) {
        try {
            absSender.execute(send);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validOption(String option) {
        return StringUtils.isBlank(option);
    }
}
