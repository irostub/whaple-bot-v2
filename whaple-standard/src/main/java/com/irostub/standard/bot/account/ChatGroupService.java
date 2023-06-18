package com.irostub.standard.bot.account;

import com.irostub.domain.entity.standard.ChatGroup;
import com.irostub.domain.repository.ChatGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Chat;

@RequiredArgsConstructor
@Service
public class ChatGroupService {
    private final ChatGroupRepository chatGroupRepository;

    @Transactional
    public ChatGroup chatGroupSaveIfNotExists(Chat chat) {
        Long groupId = chat.getId();
        String groupTitle = chat.getTitle();
        ChatGroup chatGroup = chatGroupRepository.findByChatGroupId(groupId).orElseGet(() -> ChatGroup.create(groupId, groupTitle));
        return chatGroupRepository.save(chatGroup);
    }
}
