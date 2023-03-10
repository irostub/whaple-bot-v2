package com.irostub.whaple.bot.account;

import com.irostub.whaple.bot.persistence.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Account extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long accountId;
    private String firstName;
    private String lastName;
    private String username;
    private LocalDate birthday;
    private Boolean checkIo = false;
    private Long userChatId;

    @OneToMany(mappedBy = "account")
    private List<ChatGroupUser> chatGroupUser = new ArrayList<>();

    public static Account create(Long accountId,String firstName, String lastName, String username) {
        Account account = new Account();
        account.accountId = accountId;
        account.firstName = firstName;
        account.lastName = lastName;
        account.username = username;
        return account;
    }

    public static Account create(Long accountId,String firstName, String lastName, String username, Long userChatId) {
        Account account = new Account();
        account.accountId = accountId;
        account.firstName = firstName;
        account.lastName = lastName;
        account.username = username;
        account.userChatId = userChatId;
        return account;
    }

    public String getName(){
        return (StringUtils.isNotEmpty(lastName) ? lastName : "") + firstName;
    }

    public Boolean switchCheckIo(){
        if (checkIo == null) {
            checkIo = false;
        }
        this.checkIo = !checkIo;
        return checkIo;
    }

    public void updateUserChatId(Long userChatId){
        this.userChatId = userChatId;
    }
}
