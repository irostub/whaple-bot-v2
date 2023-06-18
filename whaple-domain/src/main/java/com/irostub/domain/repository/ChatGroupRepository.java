package com.irostub.domain.repository;

import com.irostub.domain.entity.standard.ChatGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatGroupRepository extends JpaRepository<ChatGroup, Long> {
    Optional<ChatGroup> findByChatGroupId(Long chatGroupId);
}
