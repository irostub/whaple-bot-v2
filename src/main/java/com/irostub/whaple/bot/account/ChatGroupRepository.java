package com.irostub.whaple.bot.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatGroupRepository extends JpaRepository<ChatGroup, Long> {
    Optional<ChatGroup> findByChatGroupId(Long chatGroupId);
}
