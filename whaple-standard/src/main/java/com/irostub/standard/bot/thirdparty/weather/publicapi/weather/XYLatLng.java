package com.irostub.standard.bot.thirdparty.weather.publicapi.weather;

import lombok.Data;

@Data
public class XYLatLng {
    public double lat;
    public double lng;

    public double x;
    public double y;

    public String getXString() {
        return String.format("%.0f",x);
    }

    public String getYString() {
        return String.format("%.0f",y);
    }
}
