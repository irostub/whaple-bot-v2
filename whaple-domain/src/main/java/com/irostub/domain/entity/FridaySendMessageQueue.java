package com.irostub.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FridaySendMessageQueue {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatId;
    private Integer messageId;

    public static FridaySendMessageQueue create(Long chatId, Integer messageId){
        FridaySendMessageQueue fridaySendMessageQueue = new FridaySendMessageQueue();
        fridaySendMessageQueue.messageId = messageId;
        fridaySendMessageQueue.chatId = chatId;
        return fridaySendMessageQueue;
    }
}
