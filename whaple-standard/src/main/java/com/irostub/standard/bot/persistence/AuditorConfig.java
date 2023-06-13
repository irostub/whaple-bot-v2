package com.irostub.standard.bot.persistence;

import com.irostub.standard.bot.config.AccountHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Slf4j
@Configuration
public class AuditorConfig {
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            AccountHolder accountHolder = new AccountHolder();
            Long accountId = accountHolder.getUser().getAccountId();
            if (accountId == null) {
                return Optional.empty();
            }
            return Optional.of(accountId.toString());
        };
    }
}
