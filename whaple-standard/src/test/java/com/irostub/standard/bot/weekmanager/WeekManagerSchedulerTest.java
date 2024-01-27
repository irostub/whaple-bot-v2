package com.irostub.standard.bot.weekmanager;

import com.irostub.domain.entity.standard.WeekManager;
import com.irostub.domain.repository.WeekManagerRepository;
import com.irostub.standard.bot.config.JpaConfig;
import com.irostub.standard.bot.config.QueryDslConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(
        properties = {"spring.cloud.config.enabled=false"}
)
@Import({JpaConfig.class, QueryDslConfig.class})
@AutoConfigureTestDatabase
class WeekManagerSchedulerTest {

    @Autowired
    WeekManagerRepository weekManagerRepository;

    @Test
    void teststst() {
        //테스트의 오늘
        LocalDate now = LocalDate.of(2024, Month.FEBRUARY, 2);
        //직전 월요일 ~ 금요일
        LocalDate nearMon = LocalDate.of(2024, Month.FEBRUARY, 5);
        LocalDate nearFri = LocalDate.of(2024, Month.FEBRUARY, 9);
        //1주 뒤 월요일 ~ 금요일
        LocalDate after1Mon = LocalDate.of(2024, Month.FEBRUARY, 12);
        LocalDate after1Fri = LocalDate.of(2024, Month.FEBRUARY, 16);
        //2주 뒤 월요일 ~ 금요일
        LocalDate after2Mon = LocalDate.of(2024, Month.FEBRUARY, 19);
        LocalDate after2Fri = LocalDate.of(2024, Month.FEBRUARY, 23);
        //3주 뒤 월요일 ~ 금요일
        LocalDate after3Mon = LocalDate.of(2024, Month.FEBRUARY, 26);
        LocalDate after3Fri = LocalDate.of(2024, Month.MARCH, 1);

        List<WeekManager> weekManagers = List.of(
                WeekManager.create("shin", nearMon, nearFri),
                WeekManager.create("kim", after1Mon, after1Fri),
                WeekManager.create("cho", after2Mon, after2Fri),
                WeekManager.create("jin", after3Mon, after3Fri)
        );

        weekManagerRepository.saveAllAndFlush(weekManagers);

        LocalDate day3 = now.plusDays(3);
        List<WeekManager> day3Targets = weekManagerRepository.findByManageStartLessThanEqual(day3);

        LocalDate day10 = now.plusDays(10);
        List<WeekManager> day10Targets = weekManagerRepository.findByManageStartGreaterThanAndManageStartLessThanEqual(day3, day10);

        LocalDate day17 = now.plusDays(17);
        List<WeekManager> day17Targets = weekManagerRepository.findByManageStartGreaterThanAndManageStartLessThanEqual(day10, day17);

        StringBuilder sb = new StringBuilder();
        sb.append("-------[주간 담당자 알림]--------\n");
        for (WeekManager day3Target : day3Targets) {
            sb.append("다음 주 주간 담당자 ").append(day3Target.getManagerName()).append("님 잘 부탁드려요!").append("\n");
        }
        sb.append("-----------------------------\n");
        for (WeekManager day10Target : day10Targets) {
            sb.append(day10Target.getManagerName()).append("님 1주 뒤에 주간 담당자에요!").append("\n");
        }
        sb.append("-----------------------------\n");
        for (WeekManager day17Target : day17Targets) {
            sb.append(day17Target.getManagerName()).append("님 2주 뒤에 주간 담당자에요!").append("\n");
        }

        System.out.println(sb);
    }
}