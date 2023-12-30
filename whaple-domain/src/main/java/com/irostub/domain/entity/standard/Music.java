package com.irostub.domain.entity.standard;

import com.irostub.domain.entity.BaseEntity;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Entity
public class Music extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String url;
    private String videoId;

    protected Music() {
    }

    public Music(String title, String description, String url, String videoId) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.videoId = videoId;
    }
}
