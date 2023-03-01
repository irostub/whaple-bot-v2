package com.irostub.whaple.bot.fridaylunch;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FridaySendMessageQueueRepository extends JpaRepository<FridaySendMessageQueue, Long> {
}
