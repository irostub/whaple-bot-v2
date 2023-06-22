package com.irostub.market.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class InitData {
    private static final String HASH_FILED_KEY = "hash=";
    @NotBlank
    private String dataCheckString;
    @NotBlank
    private String hash;

    @JsonCreator
    public InitData(Map<String, Object> jsonNode) {
        if (jsonNode.containsKey("initData")) {
            String initDataStr = (String) jsonNode.get("initData");

            String urlDecoded = URLDecoder.decode(initDataStr, StandardCharsets.UTF_8);

            this.dataCheckString = Arrays.stream(urlDecoded.split("&"))
                    .peek(s -> {
                        if (s.startsWith(HASH_FILED_KEY)) {
                            this.hash = s.substring(HASH_FILED_KEY.length());
                        }
                    })
                    .filter(s -> !s.startsWith(HASH_FILED_KEY))
                    .collect(Collectors.joining("\n"));
        }
    }
}
