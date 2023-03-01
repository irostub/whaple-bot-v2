package com.irostub.whaple.bot.thirdparty.weather.publicapi.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {
    private Map<Category, FixedShortTermWeatherData> fixedWeatherDataMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    @JsonProperty("response")
    public void unpackNested(Map<String, Object> response) {
        Map<String, Object> body = (Map<String, Object>) response.get("body");
        Map<String, Object> items = (Map<String, Object>) body.get("items");
        List<ShortTermWeatherData> item = ((List<Object>) items.get("item")).stream()
                .map(o -> (Map<String, Object>) o)
                .map(ShortTermWeatherData::new)
                .collect(Collectors.toList());

        this.fixedWeatherDataMap = item.stream()
                .collect(Collectors.toMap(
                        ShortTermWeatherData::getCategory,
                        i -> new FixedShortTermWeatherData(i.getCategory(), i.getBaseDate(), i.getBaseTime(), i.getObsrValue())));
    }
}
