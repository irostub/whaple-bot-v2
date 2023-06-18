package com.irostub.domain.entity.market;

import com.irostub.domain.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class JungoBoardPhoto extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String origFileName;

    @Column(nullable = false)
    private String filePath;

    private Long fileSize;

    @ManyToOne
    private JungoBoard jungoBoard;

    public void setBoard(JungoBoard board){
        this.jungoBoard = board;

        if(!board.getJungoBoardPhotos().contains(this))
            board.getJungoBoardPhotos().add(this);
    }
}
