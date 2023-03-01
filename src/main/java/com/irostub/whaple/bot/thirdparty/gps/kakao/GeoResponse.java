package com.irostub.whaple.bot.thirdparty.gps.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoResponse {
    private List<Document> documents = new ArrayList<>();
}
