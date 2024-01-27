package com.irostub.domain.repository;

import com.irostub.domain.entity.standard.WeekManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface WeekManagerRepository extends JpaRepository<WeekManager, Integer> {
    List<WeekManager> findAllByOrderByManageStartAsc();

    List<WeekManager> findByManageStartLessThanEqual(LocalDate manageStart);

    List<WeekManager> findByManageStartGreaterThanAndManageStartLessThanEqual(LocalDate less, LocalDate greater);
}
