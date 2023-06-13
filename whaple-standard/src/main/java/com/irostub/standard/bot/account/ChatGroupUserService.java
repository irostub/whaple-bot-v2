package com.irostub.standard.bot.account;

import com.irostub.domain.entity.Account;
import com.irostub.domain.entity.ChatGroup;
import com.irostub.domain.entity.ChatGroupUser;
import com.irostub.domain.repository.ChatGroupUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

@RequiredArgsConstructor
@Service
public class ChatGroupUserService {
    private final ChatGroupUserRepository chatGroupUserRepository;
    private final AccountService accountService;
    private final ChatGroupService chatGroupService;

    @Transactional
    public ChatGroupUser chatGroupUserSaveIfNotExists(Account account, ChatGroup chatGroup) {
        ChatGroupUser chatGroupUser = chatGroupUserRepository.findByAccountAndChatGroup(account, chatGroup)
                .orElseGet(()->ChatGroupUser.create(account, chatGroup));
        chatGroupUserRepository.save(chatGroupUser);
        return chatGroupUser;
    }

    @Transactional
    public ChatGroupUser chatGroupUserSaveIfNotExists(User user, Chat chat) {
        Account account = accountService.accountSaveIfNotExists(user);
        ChatGroup chatGroup = chatGroupService.chatGroupSaveIfNotExists(chat);
        return chatGroupUserSaveIfNotExists(account, chatGroup);
    }
}
