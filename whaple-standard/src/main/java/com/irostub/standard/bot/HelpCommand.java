package com.irostub.standard.bot;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collection;

@Slf4j
@Component
public class HelpCommand extends ManCommand {

    private static final String COMMAND_IDENTIFIER = "도움말";
    private static final String COMMAND_DESCRIPTION = "모든 명령어를 보여줍니다. {i}도움말 [명령어] 을 사용하여 명령의 사용 방법을 볼 수 있습니다.";
    private static final String EXTENDED_DESCRIPTION = "이 명령은 봇에서 사용할 수 있는 모든 명령을 표시합니다.\n {i}도움말 [명령어] 을 사용하여 더 자세한 명령을 확인할 수 있습니다.\n 문의사항은 쿠버네티스그룹 신동민을 찾아주세요.";

    public static String getHelpText(IBotCommand...botCommands) {
        StringBuilder reply = new StringBuilder();
        for (IBotCommand com : botCommands) {
            if(com.isPublicCommand()){
                reply.append(com.toString()).append(System.lineSeparator()).append(System.lineSeparator());
            }
        }
        return reply.toString();
    }

    public static String getHelpText(Collection<IBotCommand> botCommands) {
        return getHelpText(botCommands.toArray(new IBotCommand[botCommands.size()]));
    }

    public static String getHelpText(ICommandHolder registry) {
        return getHelpText(registry.getRegisteredCommands());
    }

    public static String getManText(IBotCommand command) {
        try {
            return IManCommand.class.isInstance(command) ? getManText((IManCommand) command) : command.toString();
        } catch (NullPointerException e) {
            log.info("not support help command? command is {}", command);
            return null;
        }
    }

    public static String getManText(IManCommand command) {
        return command.toMan();
    }

    public HelpCommand() {
        super(COMMAND_IDENTIFIER, COMMAND_DESCRIPTION, EXTENDED_DESCRIPTION);
    }

    public HelpCommand(String commandIdentifier, String description, String extendedDescription) {
        super(commandIdentifier, description, extendedDescription);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        if (WhapleBot.class.isInstance(absSender)) {
            WhapleBot bot = (WhapleBot) absSender;

            if (arguments.length > 0) {
                IBotCommand command = bot.getCommandHolder().getRegisteredCommand(arguments[0]);
                String reply = getManText(command);

                if (reply == null) {
                    return;
                }

                try {
                    absSender.execute(SendMessage.builder().chatId(chat.getId()).text(reply).parseMode("HTML").build());
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                String reply = getHelpText(bot.getCommandHolder());
                try {
                    absSender.execute(SendMessage.builder().chatId(chat.getId()).text(reply).parseMode("HTML").build());
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
