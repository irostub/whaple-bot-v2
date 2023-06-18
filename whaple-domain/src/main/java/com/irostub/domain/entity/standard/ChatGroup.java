package com.irostub.domain.entity.standard;

import com.irostub.domain.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatGroup extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatGroupId;

    private String chatGroupName;

    @OneToMany(mappedBy = "chatGroup")
    private List<ChatGroupUser> chatGroupUser = new ArrayList<>();

    public static ChatGroup create(Long chatGroupId, String chatGroupName){
        ChatGroup chatGroup = new ChatGroup();
        chatGroup.chatGroupId = chatGroupId;
        chatGroup.chatGroupName = chatGroupName;
        return chatGroup;
    }
}
