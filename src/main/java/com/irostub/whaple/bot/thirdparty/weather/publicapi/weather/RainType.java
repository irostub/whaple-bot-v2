package com.irostub.whaple.bot.thirdparty.weather.publicapi.weather;

public enum RainType {
    NONE, RAIN, RAINSNOW, SHOWER, SNOW, RAINDROP, RAINDROPSNOWY, SNOWY;

    public String toNumericString() {
        int ordinal = this.ordinal();
        return String.valueOf(ordinal);
    }
}
