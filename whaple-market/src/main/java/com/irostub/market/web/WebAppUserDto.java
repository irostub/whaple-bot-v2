package com.irostub.market.web;

import com.irostub.domain.entity.market.WebAppUser;
import lombok.Data;

@Data
public class WebAppUserDto {
    private Long id;
    private String isBot;
    private String firstName;
    private String lastName;
    private String username;
    private String photoUrl;

    public WebAppUserDto(WebAppUser webAppUser) {
        this.id = webAppUser.getId();
        this.isBot = webAppUser.getIsBot();
        this.firstName = webAppUser.getFirstName();
        this.lastName = webAppUser.getLastName();
        this.username = webAppUser.getUsername();
        this.photoUrl = webAppUser.getPhotoUrl();
    }
}
