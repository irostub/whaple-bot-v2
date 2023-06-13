package com.irostub.standard.bot.thirdparty.weather.publicapi.weather;

import lombok.Data;

import java.util.Map;

@Data
public class ShortTermWeatherData {
    private String baseDate;
    private String baseTime;
    private Category category;
    private String obsrValue;

    public ShortTermWeatherData() {
    }

    public ShortTermWeatherData(Map<String, Object> data) {
        this.baseDate = (String) data.get("baseData");
        this.baseTime = (String) data.get("baseTime");
        this.category = Category.valueOf((String) data.get("category"));
        this.obsrValue = (String) data.get("obsrValue");
    }
}
