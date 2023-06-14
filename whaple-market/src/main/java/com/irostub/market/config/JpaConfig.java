package com.irostub.market.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("com.irostub.domain")
@EnableJpaRepositories("com.irostub.domain")
@EntityScan("com.irostub.domain")
public class JpaConfig {
}
