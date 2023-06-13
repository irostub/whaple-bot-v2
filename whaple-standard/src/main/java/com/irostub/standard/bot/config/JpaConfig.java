package com.irostub.standard.bot.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"com.irostub.domain.repository"})
@ComponentScan(basePackages = {"com.irostub.domain.repository"})
public class JpaConfig {
}
