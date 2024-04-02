package com.irostub.market.config;

import com.irostub.market.interceptor.TelegramAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
//    private final TelegramAuthInterceptor telegramAuthInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(telegramAuthInterceptor).addPathPatterns("/api/**");
    }
}
