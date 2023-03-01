package com.irostub.whaple.bot.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
//                .requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
//                .setConnectTimeout(Duration.ofSeconds(5000L))
//                .setReadTimeout(Duration.ofSeconds(3000L))
//                .additionalMessageConverters(new StringHttpMessageConverter(StandardCharsets.UTF_8))
//                .build();
    }

}

