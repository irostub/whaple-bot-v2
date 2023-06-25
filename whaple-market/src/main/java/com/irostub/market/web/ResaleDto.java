package com.irostub.market.web;

import com.irostub.domain.entity.market.ImgurImage;
import com.irostub.domain.entity.market.ResaleBoard;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ResaleDto {
    private Long id;
    private String title;
    private String content;
    private List<String> images = new ArrayList<>();
    private WebAppUserDto webAppUserDto;

    public static ResaleDto createFromEntity(ResaleBoard resaleBoard) {
        ResaleDto resaleDto = new ResaleDto();
        resaleDto.setTitle(resaleBoard.getTitle());
        resaleDto.setContent(resaleBoard.getContent());
        List<String> images = resaleBoard.getImages().stream()
                .map(ImgurImage::getUrl)
                .collect(Collectors.toList());
        resaleDto.setImages(images);
        resaleDto.setWebAppUserDto(new WebAppUserDto(resaleBoard.getWebAppUser()));
        return resaleDto;
    }
}
