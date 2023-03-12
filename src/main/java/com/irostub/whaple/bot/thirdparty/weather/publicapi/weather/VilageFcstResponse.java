package com.irostub.whaple.bot.thirdparty.weather.publicapi.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class VilageFcstResponse {
    private Map<LocalDateTime, VilageFcstData> vilageFcstDataMap = new LinkedHashMap<>();
    private String minTemp;
    private String maxTemp;

    @SuppressWarnings("unchecked")
    @JsonProperty("response")
    public void unpackNested(Map<String, Object> response) {
        Map<String, Object> body = (Map<String, Object>) response.get("body");
        Map<String, Object> items = (Map<String, Object>) body.get("items");
        List<Object> item = (List<Object>) items.get("item");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

        for (Object o : item) {
            Map<String, Object> data = (Map<String, Object>) o;
            String category = (String) data.get("category");
            String fcstValue = (String) data.get("fcstValue");
            String fcstDate = (String) data.get("fcstDate");
            String fcstTime = (String) data.get("fcstTime");
            LocalDateTime localDateTime = LocalDateTime.parse(fcstDate+fcstTime, dtf);
            VilageFcstData vilageFcstData;

            if (vilageFcstDataMap.containsKey(localDateTime)) {
                vilageFcstData = vilageFcstDataMap.get(localDateTime);
            }else{
                vilageFcstData = new VilageFcstData();
            }

            switch (category) {
                case "SKY":
                    vilageFcstData.setSky(fcstValue);
                    break;
                case "REH":
                    vilageFcstData.setReh(fcstValue + " %");
                    break;
                case "PCP":
                    if(!"강수없음".equals(fcstValue)){
                        vilageFcstData.setPcp(fcstValue);
                    }
                    break;
                case "PTY":
                    vilageFcstData.setPty(fcstValue);
                    break;
                case "POP":
                    vilageFcstData.setPop(fcstValue + " %");
                    break;
                case "SNO":
                    if(!"적설없음".equals(fcstValue)){
                        vilageFcstData.setSno(fcstValue);
                    }
                    break;
                case "TMP":
                    vilageFcstData.setTmp(fcstValue + " ℃");
                    break;
                case "TMN":
                    minTemp = fcstValue + " ℃";
                    break;
                case "TMX":
                    maxTemp = fcstValue + " ℃";
                    break;
            }
            vilageFcstDataMap.put(localDateTime, vilageFcstData);
        }
    }
}
