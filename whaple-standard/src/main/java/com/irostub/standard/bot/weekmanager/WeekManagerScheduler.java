package com.irostub.standard.bot.weekmanager;

import com.irostub.domain.entity.standard.WeekManager;
import com.irostub.domain.repository.WeekManagerRepository;
import com.irostub.standard.bot.utils.TelegramUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class WeekManagerScheduler {
    private final WeekManagerRepository weekManagerRepository;
    private final AbsSender absSender;

    @Scheduled(cron = "0 30 13 * * 5", zone = "Asia/Seoul")
    public void weekManagerNotice() {
        LocalDate now = LocalDate.now();

        LocalDate day3 = now.plusDays(3);
        List<WeekManager> day3Targets = weekManagerRepository.findByManageStartLessThanEqual(day3);

        LocalDate day10 = now.plusDays(10);
        List<WeekManager> day10Targets = weekManagerRepository.findByManageStartGreaterThanAndManageStartLessThanEqual(day3, day10);

        LocalDate day17 = now.plusDays(17);
        List<WeekManager> day17Targets = weekManagerRepository.findByManageStartGreaterThanAndManageStartLessThanEqual(day10, day17);

        StringBuilder sb = new StringBuilder();
        sb.append("-------[주간 담당자 알림이]--------\n");
        if (day3Targets.isEmpty() && day10Targets.isEmpty() && day17Targets.isEmpty()) {
            sb.append("등록된 주간 담당자가 없어요. 한번 등록해볼까요?\n");
        }
        if(!day3Targets.isEmpty()){
            for (WeekManager day3Target : day3Targets) {
                sb.append("다음 주 주간 담당자 ").append(day3Target.getManagerName()).append("님 잘 부탁드려요!").append("\n");
            }
        }

        if(!day10Targets.isEmpty()){
            sb.append("------------------------------\n");
            for (WeekManager day10Target : day10Targets) {
                sb.append(day10Target.getManagerName()).append("님 1주 뒤에 주간 담당자에요!").append("\n");
            }
        }

        if(!day17Targets.isEmpty()) {
            sb.append("------------------------------\n");
            for (WeekManager day17Target : day17Targets) {
                sb.append(day17Target.getManagerName()).append("님 2주 뒤에 주간 담당자에요!").append("\n");
            }
        }


        SendMessage message = SendMessage.builder().chatId(WeekManagerCommand.ID).text(sb.toString()).build();
        TelegramUtils.sendMessage(absSender, message);

        weekManagerRepository.deleteAll(day3Targets);
    }
}
