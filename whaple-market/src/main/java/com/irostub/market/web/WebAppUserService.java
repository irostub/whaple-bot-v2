package com.irostub.market.web;

import com.irostub.domain.entity.market.WebAppUser;
import com.irostub.domain.repository.WebAppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WebAppUserService {
    private final WebAppUserRepository webAppUserRepository;
    @Transactional
    public WebAppUser getOrCreateUser(WebAppUserDto webAppUserDto) {
        Optional<WebAppUser> byId = webAppUserRepository.findById(webAppUserDto.getId());

        if (byId.isPresent()) {
            return byId.get();
        }else{
            WebAppUser webAppUser = newWebAppUser(webAppUserDto);
            return webAppUserRepository.save(webAppUser);
        }
    }

    private static WebAppUser newWebAppUser(WebAppUserDto webAppUserDto) {
        return new WebAppUser(
                webAppUserDto.getId(),
                webAppUserDto.getIsBot(),
                webAppUserDto.getFirstName(),
                webAppUserDto.getLastName(),
                webAppUserDto.getUsername(),
                webAppUserDto.getPhotoUrl()
        );
    }
}
