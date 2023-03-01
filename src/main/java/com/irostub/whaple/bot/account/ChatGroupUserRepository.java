package com.irostub.whaple.bot.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatGroupUserRepository extends JpaRepository<ChatGroupUser, Long> {
    Optional<ChatGroupUser> findByAccountAndChatGroup(Account account, ChatGroup chatGroup);
}
