package com.irostub.domain.entity.market;

import com.irostub.domain.entity.BaseEntity;
import lombok.Getter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class ResaleBoard extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private Boolean soldout = false;

    @OneToMany(mappedBy = "resaleBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImgurImage> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private WebAppUser webAppUser;

    protected ResaleBoard() {
    }

    public ResaleBoard(String title, String content, List<ImgurImage> images, WebAppUser webAppUser) {
        this.title = title;
        this.content = content;
        this.images = images;
        if (images != null) {
            for (ImgurImage image : images) {
                image.setResaleBoard(this);
            }
        }
        this.webAppUser = webAppUser;
    }

    public void soldout() {
        this.soldout = true;
    }

    public void updatePost(String title,
                           String content,
                           List<ImgurImage> images) {
        this.title = title;
        this.content = content;
        this.images.addAll(images);
    }
}
