package com.irostub.whaple.bot.account;

import com.irostub.whaple.bot.persistence.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
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

    public String getName(){
        return (StringUtils.isNotEmpty(lastName) ? lastName : "") + firstName;
    }
}
