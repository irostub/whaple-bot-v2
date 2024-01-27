package com.irostub.domain.entity.standard;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Entity
public class WeekManager {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String managerName;
    private LocalDate manageStart;
    private LocalDate manageEnd;

    public static WeekManager create(String managerName, LocalDate manageStart, LocalDate manageEnd) {
        WeekManager weekManager = new WeekManager();
        weekManager.managerName = managerName;
        weekManager.manageStart = manageStart;
        weekManager.manageEnd = manageEnd;
        return weekManager;
    }
}
