package com.irostub.market.interceptor;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Getter
public class InitData {
    private static final String HASH_FILED_KEY = "hash=";
    private static final String INIT_DATA_KEY = "initData";
    private static final String SPLIT_CHAR = "&";

    @NotBlank
    private String dataCheckString;
    @NotBlank
    private String hash;

    @JsonCreator
    public InitData(Map<String, Object> jsonNode) {
        if (jsonNode.containsKey(INIT_DATA_KEY)) {
            String initDataStr = (String) jsonNode.get(INIT_DATA_KEY);
            String urlDecoded = URLDecoder.decode(initDataStr, StandardCharsets.UTF_8);

            this.dataCheckString = Arrays.stream(urlDecoded.split(SPLIT_CHAR))
                    .peek(s -> {
                        if (s.startsWith(HASH_FILED_KEY)) {
                            this.hash = s.substring(HASH_FILED_KEY.length());
                        }
                    })
                    .filter(s -> !s.startsWith(HASH_FILED_KEY))
                    .sorted()
                    .collect(Collectors.joining("\n"));
        }
    }
}
