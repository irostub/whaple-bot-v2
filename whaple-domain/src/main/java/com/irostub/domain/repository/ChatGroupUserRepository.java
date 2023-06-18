package com.irostub.domain.repository;

import com.irostub.domain.entity.standard.Account;
import com.irostub.domain.entity.standard.ChatGroup;
import com.irostub.domain.entity.standard.ChatGroupUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatGroupUserRepository extends JpaRepository<ChatGroupUser, Long> {
    Optional<ChatGroupUser> findByAccountAndChatGroup(Account account, ChatGroup chatGroup);
}
