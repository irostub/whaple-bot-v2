package com.irostub.standard.bot.thirdparty.gps.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Document {
    @JsonProperty("address_name")
    private String addressName;
    private Double x;
    private Double y;
}
