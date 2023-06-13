package com.irostub.standard.bot.thirdparty.weather.publicapi.weather;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class VilageFcstData {
    private String sky;
    private String reh;
    private String pcp;
    private String pty;
    private String pop;
    private String sno;
    private String tmp;

    public void setSky(String sky) {
        int code = Integer.parseInt(sky);
        switch (code) {
            case 1:
                this.sky = "☀️";
                break;
            case 3:
                this.sky = "⛅";
                break;
            case 4:
                this.sky = "☁️";
                break;
            default:
                this.sky = null;
        }
    }

    public void setPty(String pty) {
        int code = Integer.parseInt(pty);
        switch (code) {
            case 1:
                this.pty = "\uD83C\uDF27️";
                break;
            case 2:
                this.pty = "\uD83C\uDF28️";
                break;
            case 3:
                this.pty = "❄️";
                break;
            case 4:
                this.pty = "\uD83C\uDF26";
                break;
            default:
                this.pty = null;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(StringUtils.isNotEmpty(pop)){
            sb.append("강수확률 : ").append(pop).append("\n");
        }
        if(StringUtils.isNotEmpty(tmp)){
            sb.append("기온 : ").append(tmp).append("\n");
        }
        if(StringUtils.isNotEmpty(sky)){
            sb.append("하늘 : ").append(sky).append("\n");
        }
        if(StringUtils.isNotEmpty(pcp)){
            sb.append("강수량 : ").append(pcp).append("\n");
        }
        if(StringUtils.isNotEmpty(pty)){
            sb.append("강수형태 : ").append(pty).append("\n");
        }
        if(StringUtils.isNotEmpty(sno)){
            sb.append("적설량 : ").append(sno).append("\n");
        }
        if(StringUtils.isNotEmpty(reh)){
            sb.append("습도 : ").append(reh).append("\n");
        }
        return sb.toString();
    }
}
