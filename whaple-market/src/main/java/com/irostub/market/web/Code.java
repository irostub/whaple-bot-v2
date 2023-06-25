package com.irostub.market.web;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Code {
    UNAUTHORIZED(9401), OK(9200);

    public final Integer code;

    Code(Integer code) {
        this.code = code;
    }

    @JsonValue
    public Integer getCode() {
        return code;
    }
}
