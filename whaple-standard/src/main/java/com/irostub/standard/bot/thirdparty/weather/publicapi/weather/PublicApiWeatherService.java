package com.irostub.standard.bot.thirdparty.weather.publicapi.weather;

import com.irostub.standard.bot.config.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Service
public class PublicApiWeatherService {
    private final AppProperties appProperties;

    public VilageFcstResponse requestShortTermWithGrid(String nx, String ny) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        String yyyyMMdd = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        UriComponents uriComponents = UriComponentsBuilder
                .fromUriString(appProperties.getPublicApi().getWeather().getUrl() + "/getVilageFcst")
                .queryParam("serviceKey", appProperties.getPublicApi().getWeather().getToken())
                .queryParam("pageNo", 1)
                .queryParam("numOfRows", 500)
                .queryParam("dataType", "JSON")
                .queryParam("base_date", yyyyMMdd)
                .queryParam("base_time", "0200")
                .queryParam("nx", nx)
                .queryParam("ny", ny)
                .build(false);

        DefaultUriBuilderFactory defaultUriBuilderFactory = new DefaultUriBuilderFactory();
        defaultUriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(defaultUriBuilderFactory);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<VilageFcstResponse> exchange = restTemplate.exchange(uriComponents.toUriString(), HttpMethod.GET, entity, VilageFcstResponse.class);

        return exchange.getBody();
    }

    public Map<Category, FixedShortTermWeatherData> requestWithGrid(String nx, String ny){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        String yyyy = now.format(DateTimeFormatter.ofPattern("yyyy"));
        String month = now.format(DateTimeFormatter.ofPattern("MM"));
        String dd = now.format(DateTimeFormatter.ofPattern("dd"));
        int mm = now.getMinute();
        int hour = now.getHour();
        if (mm < 31) {
            hour -= 1;
        }
        if (hour == -1){
            hour = 23;
            LocalDateTime localDateTime = now.minusDays(-1L);
            dd = localDateTime.format(DateTimeFormatter.ofPattern("dd"));
        }
        String baseDateFormatted = yyyy+month+dd;
        String baseTimeFormatted = String.format("%02d", hour)+"00";

        UriComponents uriComponents = UriComponentsBuilder
                .fromUriString(appProperties.getPublicApi().getWeather().getUrl() + "/getUltraSrtNcst")
                .queryParam("serviceKey", appProperties.getPublicApi().getWeather().getToken())
                .queryParam("pageNo", 1)
                .queryParam("numOfRows", 10)
                .queryParam("dataType", "JSON")
                .queryParam("base_date", baseDateFormatted)
                .queryParam("base_time", baseTimeFormatted)
                .queryParam("nx", nx)
                .queryParam("ny", ny)
                .build(false);
        DefaultUriBuilderFactory defaultUriBuilderFactory = new DefaultUriBuilderFactory();
        defaultUriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(defaultUriBuilderFactory);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<WeatherResponse> exchange1 = restTemplate.exchange(uriComponents.toUriString(), HttpMethod.GET, entity, WeatherResponse.class);
        log.info(exchange1.getStatusCode().toString());
        log.info("ResponseEntity: " + exchange1);
        return exchange1.getBody().getFixedWeatherDataMap();
    }
}
