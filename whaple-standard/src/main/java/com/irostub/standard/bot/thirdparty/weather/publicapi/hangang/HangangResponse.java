package com.irostub.standard.bot.thirdparty.weather.publicapi.hangang;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HangangResponse {
    private Map<String, RiverData> data = new HashMap<>();

    @SuppressWarnings("unchecked")
    @JsonProperty("WPOSInformationTime")
    public void unpackNested(Map<String, Object> wposInformationTime) {
        this.data = ((List<Object>)wposInformationTime.get("row")).stream()
                .map(o->(Map<String, Object>) o)
                .map(RiverData::new)
                .collect(Collectors.toMap(RiverData::getLocation, r->r));
    }

    public RiverData getHangangData(){
        return this.data.get("중랑천");
   }
}
