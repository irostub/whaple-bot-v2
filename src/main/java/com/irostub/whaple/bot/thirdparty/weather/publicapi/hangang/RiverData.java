package com.irostub.whaple.bot.thirdparty.weather.publicapi.hangang;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Getter @Setter
public class RiverData {
    private LocalDateTime measureDateTime;
    private String Location;
    private Float temperature;
    private boolean inspection;

    public RiverData(Map<String, Object> rowData) {
        String msrDateTime = String.valueOf(rowData.get("MSR_DATE")) + rowData.get("MSR_TIME");
        String siteId = String.valueOf(rowData.get("SITE_ID"));
        String temperature = String.valueOf(rowData.get("W_TEMP"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHH:mm");

        this.measureDateTime = LocalDateTime.parse(msrDateTime, formatter);
        this.Location = siteId;
        try {
            this.temperature = Float.valueOf(temperature);
        }catch (NumberFormatException e){
            this.temperature = null;
            inspection = true;
        }
    }
}
