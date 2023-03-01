package com.irostub.whaple.bot.thirdparty.weather.publicapi.weather;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum Category {
    T1H,    //기온
    RN1,    //1시간 강수량
    UUU,    //동서바람성분
    VVV,    //남북바람성분
    REH,    //습도
    PTY,    //강수형태
    VEC,    //풍향
    WSD;    //풍속

    @JsonCreator
    public static Category jsonCreator(String value) {
        return Arrays.stream(Category.values())
                .filter(t -> t.name().equals(value))
                .findFirst()
                .orElseThrow();
    }
}
