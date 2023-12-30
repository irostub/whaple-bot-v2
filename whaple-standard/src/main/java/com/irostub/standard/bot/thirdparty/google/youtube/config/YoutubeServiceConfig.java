package com.irostub.standard.bot.thirdparty.google.youtube.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;

import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.irostub.standard.bot.config.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Configuration
@RequiredArgsConstructor
public class YoutubeServiceConfig {
    private final JsonFactory json = JacksonFactory.getDefaultInstance();
    private final AppProperties appProperties;

    @Bean
    public YouTube youTube() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        HttpRequestInitializer httpRequestInitializer = request -> {
            request.setInterceptor(intercepted -> intercepted.getUrl().set("key", appProperties.getYoutubeToken()));
        };

        return new YouTube.Builder(httpTransport, json, httpRequestInitializer)
                .setApplicationName(appProperties.getYoutubeAppName())
                .build();
    }
}
