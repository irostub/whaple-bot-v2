package com.irostub.domain.entity.market;

import com.irostub.domain.entity.BaseEntity;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Getter
@Entity
public class ImgurImage extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    private String imgurImageId;
    private String deleteHash;
    @ManyToOne(fetch = FetchType.LAZY)
    private ResaleBoard resaleBoard;

    protected ImgurImage() {
    }

    public ImgurImage(String url, String imgurImageId, String deleteHash) {
        this.url = url;
        this.imgurImageId = imgurImageId;
        this.deleteHash = deleteHash;
    }

    public void setResaleBoard(ResaleBoard resaleBoard) {
        this.resaleBoard = resaleBoard;
    }
}
