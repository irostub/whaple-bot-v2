package com.irostub.domain.entity.market;


import com.irostub.domain.entity.BaseEntity;
import com.irostub.domain.entity.standard.Account;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class JungoBoard extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String post;

    private String title;

    private Integer price;

    @ManyToOne
    private Account account;

    @OneToMany(mappedBy = "jungoBoard")
    private List<JungoBoardPhoto> jungoBoardPhotos = new ArrayList<>();
}
