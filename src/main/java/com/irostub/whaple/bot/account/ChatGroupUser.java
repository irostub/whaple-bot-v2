package com.irostub.whaple.bot.account;

import com.irostub.whaple.bot.persistence.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity @Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"chatGroup","account"})})
public class ChatGroupUser extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatGroupUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatGroup")
    private ChatGroup chatGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account")
    private Account account;

    public static ChatGroupUser create(Account account, ChatGroup chatGroup) {
        ChatGroupUser chatGroupUser = new ChatGroupUser();
        chatGroupUser.account = account;
        chatGroupUser.chatGroup = chatGroup;

        account.getChatGroupUser().add(chatGroupUser);
        chatGroup.getChatGroupUser().add(chatGroupUser);

        return chatGroupUser;
    }
}
