package com.irostub.market.thirdparty.imgur;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
public class ImgurImagePostResponse {
    private Data data;
    private Boolean success;
    private Integer status;

    @Getter
    @Setter
    public static class Data {
        private String id;
        private String title;
        private String description;
        private String datetime;
        private String type;
        private String animated;
        private String width;
        private String height;
        private String size;
        private String views;
        private String bandwidth;
        private String vote;
        private String favorite;
        private String nsfw;
        private String section;
        private String account_url;
        private String account_id;
        private String is_ad;
        private String in_most_viral;
        private List<String> tags = new ArrayList<>();
        private String ad_type;
        private String ad_url;
        private String in_gallery;
        private String deletehash;
        private String name;
        private String link;
    }
}