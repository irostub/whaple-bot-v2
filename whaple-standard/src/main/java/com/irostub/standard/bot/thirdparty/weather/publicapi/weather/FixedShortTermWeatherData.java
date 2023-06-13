package com.irostub.standard.bot.thirdparty.weather.publicapi.weather;

import lombok.Data;

@Data
public class FixedShortTermWeatherData {
    private String baseDate;
    private String baseTime;
    private String obsrValue;

    public FixedShortTermWeatherData(Category category, String baseDate, String baseTime, String obsrValue) {
        this.baseDate = baseDate;
        this.baseTime = baseTime;
        this.obsrValue = "";
        switch (category) {
            case T1H:
                this.obsrValue = obsrValue + "℃";
                break;
            case RN1:
                this.obsrValue = obsrValue + "mm";
                break;
            case UUU:
            case VVV:
                this.obsrValue = obsrValue + "m/s";
                break;
            case REH: {
                Float humidity = Float.valueOf(obsrValue);
                String emoji = "💧";
                if (humidity < 20) {
                    this.obsrValue = emoji;
                } else if (humidity < 40) {
                    this.obsrValue = emoji + emoji;
                } else if (humidity < 60) {
                    this.obsrValue = emoji + emoji + emoji;
                } else if (humidity < 80) {
                    this.obsrValue = emoji + emoji + emoji + emoji;
                } else {
                    this.obsrValue = emoji + emoji + emoji + emoji + emoji;
                }
            }
            break;
            case PTY: {
                if (obsrValue.equals("0")) {
                    this.obsrValue = "☀️";
                } else if (obsrValue.equals("1")) {
                    this.obsrValue = "\uD83C\uDF27️️";
                } else if (obsrValue.equals("2")) {
                    this.obsrValue = "\uD83C\uDF27️❄️";
                } else if (obsrValue.equals("3")) {
                    this.obsrValue = "❄️";
                } else if (obsrValue.equals("5")) {
                    this.obsrValue = "☔";
                } else if (obsrValue.equals("6")) {
                    this.obsrValue = "☔❄️";
                } else if (obsrValue.equals("7")) {
                    this.obsrValue = "❄️";
                }
            }
            break;
            case VEC: {
                double value = Double.parseDouble(obsrValue);
                if ((value > 337.5 && value <= 360) || (value >= 0 && value <= 22.5)) {
                    this.obsrValue = "⬆️";
                } else if (value > 22.5 && value <= 67.5) {
                    this.obsrValue = "↗️";
                } else if (value > 67.5 && value <= 112.5) {
                    this.obsrValue = "➡️";
                } else if (value > 112.5 && value <= 157.5) {
                    this.obsrValue = "↘️";
                } else if (value > 157.5 && value <= 202.5) {
                    this.obsrValue = "⬇️";
                } else if (value > 202.5 && value <= 247.5) {
                    this.obsrValue = "↙️";
                } else if (value > 247.5 && value <= 292.5) {
                    this.obsrValue = "⬅️";
                } else if (value > 292.5 && value <= 337.5) {
                    this.obsrValue = "↖️";
                }
            }
            break;
            case WSD: {
                Float wind = Float.valueOf(obsrValue);
                String emoji = "\uD83D\uDCA8";
                if (wind < 4) {
                    this.obsrValue = emoji;
                } else if (wind < 9) {
                    this.obsrValue = emoji + emoji;
                } else if (wind < 14) {
                    this.obsrValue = emoji + emoji + emoji;
                } else if (wind < 19) {
                    this.obsrValue = emoji + emoji + emoji + emoji;
                } else {
                    this.obsrValue = emoji + emoji + emoji + emoji + emoji;
                }
            }
            break;
        }
    }
}
