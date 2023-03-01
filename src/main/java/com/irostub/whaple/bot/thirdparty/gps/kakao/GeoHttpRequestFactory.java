package com.irostub.whaple.bot.thirdparty.gps.kakao;

import com.irostub.whaple.bot.config.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GeoHttpRequestFactory {
    private final AppProperties properties;

    public HttpEntity<?> getHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + properties.getKakao().getGeo().getToken());

        return new HttpEntity<>(headers);
    }
}
