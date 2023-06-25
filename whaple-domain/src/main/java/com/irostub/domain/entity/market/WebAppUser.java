package com.irostub.domain.entity.market;

import com.irostub.domain.entity.BaseEntity;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
public class WebAppUser extends BaseEntity {
    @Id
    private Long id;
    private String isBot;
    private String firstName;
    private String lastName;
    private String username;
    private String photoUrl;
    private String fullname1;
    private String fullname2;


    protected WebAppUser(){

    }

    public WebAppUser(Long id, String isBot, String firstName, String lastName, String username, String photoUrl) {
        this.id = id;
        this.isBot = isBot;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.photoUrl = photoUrl;
        this.fullname1 = StringUtils.defaultString(firstName, "") + StringUtils.defaultString(lastName, "");
        this.fullname2 = StringUtils.defaultString(lastName, "") + StringUtils.defaultString(firstName, "");
    }
}
