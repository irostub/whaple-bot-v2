package com.irostub.standard.bot;

import com.irostub.domain.entity.Account;
import com.irostub.standard.bot.account.AccountService;
import com.irostub.standard.bot.account.ChatGroupUserService;
import com.irostub.standard.bot.config.AccountHoldInstance;
import com.irostub.standard.bot.config.AccountHolder;
import com.irostub.standard.bot.config.AppProperties;
import com.irostub.standard.bot.fridaylunch.FridayCallbackAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Slf4j
@Component
public class WhapleBot extends TelegramLongPollingBot {
    private final CommandHolder commandHolder;
//    private final CallBackHolder callBackHolder;
    private final AppProperties properties;

    private final ChatGroupUserService chatGroupUserService;
    private final AccountService accountService;
    private final FridayCallbackAction fridayCallbackAction;
    @Autowired
    public WhapleBot(CommandHolder commandHolder,
                     AppProperties appProperties,
                     ChatGroupUserService chatGroupUserService,
                     AccountService accountService,
                     FridayCallbackAction fridayCallbackAction) {
        super(appProperties.getBot().getToken());
        this.commandHolder = commandHolder;
        this.properties = appProperties;
        this.chatGroupUserService = chatGroupUserService;
        this.accountService = accountService;
//        this.callBackHolder = callBackHolder;
        this.fridayCallbackAction = fridayCallbackAction;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (isSupportMessageType(message) && !filter(message)) {
                preProcessAccountAndGroup(message);
                if (!commandHolder.executeCommand(this, message)) {
                    processInvalidCommandUpdate(update);
                }
                return;
            }
        }
        processNonCommandUpdate(update);
    }

    private void holdAccountInfo(Long userId) {
        Account account = accountService.findByIdWithNull(userId);
        AccountHolder accountHolder = new AccountHolder();
        AccountHoldInstance accountHoldInstance = new AccountHoldInstance(account.getId(), account.getAccountId());
        accountHolder.setUser(accountHoldInstance);
    }

    private void preProcessAccountAndGroup(Message message) {
        if(message.isGroupMessage()){
            chatGroupUserService.chatGroupUserSaveIfNotExists(message.getFrom(), message.getChat());
        }
        if (message.isUserMessage()) {
            accountService.accountSaveWithChatId(message.getChat().getId(), message.getFrom());
        }
        if (message.isSuperGroupMessage()) {
            chatGroupUserService.chatGroupUserSaveIfNotExists(message.getFrom(), message.getChat());
        }
        try {
            holdAccountInfo(message.getFrom().getId());
        } catch (NullPointerException e) {
            log.error("NPE, chat={}", message);
        }
    }

    private boolean isSupportMessageType(Message message){
        return message.isUserMessage() || message.isGroupMessage() || message.isSuperGroupMessage();
    }

    @Override
    public String getBotUsername() {
        return properties.getBot().getBotUsername();
    }

    @Override
    public void onRegister() {
        log.info("whaple bot is running!");
    }

    private boolean filter(Message message) {
        if (message.getFrom().getIsBot()) {
            return true;
        }
        return false;
    }

    void processNonCommandUpdate(Update update){
        if(update.hasCallbackQuery()){
            if(update.getCallbackQuery().getFrom().getIsBot()){
                return;
            }

            User user = update.getCallbackQuery().getFrom();
            accountService.accountSaveIfNotExists(user);

            if (update.getCallbackQuery().getData().equals("friday")) {
                fridayCallbackAction.callback(this, update);
            }
        }
    }

    void processInvalidCommandUpdate(Update update){
        processNonCommandUpdate(update);
    }


    public CommandHolder getCommandHolder() {
        return commandHolder;
    }
}
