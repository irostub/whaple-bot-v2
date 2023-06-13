package com.irostub.standard.bot.thirdparty.gps.kakao;

import com.irostub.standard.bot.config.AppProperties;
import io.netty.channel.ChannelOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Slf4j
@Service
public class GeoService {
    private final AppProperties properties;

    private WebClient client;

    @PostConstruct
    public void init() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);

        client = WebClient.builder()
                .baseUrl(properties.getKakao().getGeo().getBaseUrl())
                .defaultHeader("Authorization", "KakaoAK " +
                        properties.getKakao().getGeo().getToken())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    public Mono<GeoResponse> getGeo(String location) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(properties.getKakao().getGeo().getUrl())
                        .queryParam("query", location)
                        .build(false))
                .retrieve()
                .bodyToMono(GeoResponse.class);
    }

    public Mono<GeoResponse> getGeoKeyword(String location) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(properties.getKakao().getGeo().getKeywordUrl())
                        .queryParam("query", location)
                        .build(false))
                .retrieve()
                .bodyToMono(GeoResponse.class);
    }
}
