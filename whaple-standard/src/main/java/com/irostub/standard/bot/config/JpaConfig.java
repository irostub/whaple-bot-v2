package com.irostub.standard.bot.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("com.irostub.domain")
@EnableJpaRepositories("com.irostub.domain")
public class JpaConfig {
}
