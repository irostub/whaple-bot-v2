package com.irostub.standard.bot.weekmanager;

import com.irostub.domain.entity.standard.WeekManager;
import com.irostub.domain.repository.WeekManagerRepository;
import com.irostub.standard.bot.DefaultBotCommand;
import com.irostub.standard.bot.IManCommand;
import com.irostub.standard.bot.utils.TelegramUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class WeekManagerCommand extends DefaultBotCommand implements IManCommand {
    private final WeekManagerRepository weekManagerRepository;
    final static String ID = "-704545630";

    public WeekManagerCommand(WeekManagerRepository weekManagerRepository) {
        super("주번", "주번을 등록하여 직전 금요일, 1주 전, 2주 전 금요일에 미리 알람을 받아 볼 수 있습니다.\n주간 담당은 월요일부터 시작이므로 당일 금요일로부터 2주 + 3일 후(월요일)의 일정, 1주 + 3일 후(월요일)의 일정을 미리 받을 수 있습니다.");
        this.weekManagerRepository = weekManagerRepository;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, Integer messageId, String[] arguments) {
        if (!ID.equals(chat.getId().toString())) {
            SendMessage accessRestrictions = SendMessage.builder()
                    .chatId(chat.getId().toString())
                    .text("해당 명령은 쿠버네티스그룹에서만 사용 가능한 명령입니다.")
                    .build();
            TelegramUtils.sendMessage(absSender, accessRestrictions);
            return;
        }

        if (arguments.length == 0) {
            //잘못된 사용 기본 설명 노출
            TelegramUtils.sendMessage(absSender, help());
            return;
        } else if (arguments.length == 1) {
            //목록
            if (arguments[0].equals("목록")) {
                List<WeekManager> all = weekManagerRepository.findAllByOrderByManageStartAsc();
                if (all.isEmpty()) {
                    SendMessage message = SendMessage.builder().chatId(ID).text("등록된 주번 일정이 하나도 없습니다.").build();
                    TelegramUtils.sendMessage(absSender, message);
                    return;
                }
                int idx = 1;
                StringBuilder line = new StringBuilder();
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                for (WeekManager weekManager : all) {
                    line(df, line, idx++, weekManager).append("\n");
                }
                SendMessage message = SendMessage.builder()
                        .text(line.toString())
                        .chatId(ID)
                        .build();
                TelegramUtils.sendMessage(absSender, message);
                return;
            } else {
                //잘못된 사용 기본 설명 노출
                TelegramUtils.sendMessage(absSender, help());
                return;
            }
        } else if (arguments.length == 2) {
            //삭제
            if (arguments[0].equals("삭제")) {
                int deleteIdx;
                try {
                    deleteIdx = Integer.parseInt(arguments[1]);
                    if (deleteIdx < 1) {
                        TelegramUtils.sendMessage(absSender, SendMessage.builder().chatId(ID).text("잘못된 삭제 id 를 사용했습니다.").build());
                        return;
                    }
                    List<WeekManager> all = weekManagerRepository.findAllByOrderByManageStartAsc();
                    if (all.size() >= deleteIdx) {
                        WeekManager weekManager = all.get(deleteIdx - 1);
                        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        StringBuilder line = new StringBuilder();
                        SendMessage deleteMessage = SendMessage.builder().chatId(ID).text("삭제 완료 - " + line(df, line, deleteIdx, weekManager)).build();
                        weekManagerRepository.delete(weekManager);
                        TelegramUtils.sendMessage(absSender, deleteMessage);
                        return;
                    } else {
                        TelegramUtils.sendMessage(absSender, SendMessage.builder().chatId(ID).text("삭제를 하려했지만 미끄러졌습니다.").build());
                        return;
                    }
                } catch (NumberFormatException e) {
                    SendMessage deleteError = SendMessage.builder()
                            .chatId(ID)
                            .text("!주번 삭제 [목록의 ID]\n" +
                                    "삭제 명령을 잘못 사용한 것 같습니다. !주번 목록 명령을 통해 ID 를 획득하여 삭제하세요. ")
                            .build();
                    TelegramUtils.sendMessage(absSender, deleteError);
                    return;
                }
            } else {
                TelegramUtils.sendMessage(absSender, help());
                return;
            }
        } else if (arguments.length == 4) {
            //등록
            if ("등록".equals(arguments[0])) {
                register(absSender, arguments);
                return;
            }
            return;
        } else if (arguments.length > 4) {
            if ("일괄등록".equals(arguments[0])) {
                bulkRegister(absSender, arguments);
                return;
            }else{
                TelegramUtils.sendMessage(absSender, help());
                return;
            }
        } else {
            TelegramUtils.sendMessage(absSender, help());
            //잘못된 사용 기본 설명 노출
            return;
        }
    }

    private SendMessage help() {
        return SendMessage.builder()
                .chatId(ID)
                .text(getExtendedDescription())
                .build();
    }

    private static StringBuilder line(DateTimeFormatter df, StringBuilder sb, int idx, WeekManager weekManager) {
        sb.append("[").append(idx).append("] ")
                .append("담당자: ").append(weekManager.getManagerName())
                .append(", 시작일: ").append(df.format(weekManager.getManageStart()))
                .append(", 종료일: ").append(df.format(weekManager.getManageEnd()));
        return sb;
    }

    private static void registerError(AbsSender absSender) {
        SendMessage message = SendMessage.builder()
                .chatId(ID)
                .text("!주번 [담당자] [시작일] [종료일]\n" +
                        "명령어 사용이 잘못되었습니다.\n" +
                        "시작일과 종료일의 형식은 yyyyMMdd 를 따릅니다.\n" +
                        "시작일은 월요일만 가능하며 종료일은 금,일요일만 가능합니다.\n")
                .build();
        TelegramUtils.sendMessage(absSender, message);
    }

    private void register(AbsSender absSender, String[] arguments) {
        String who = arguments[1];
        String startStr = arguments[2];
        String endStr = arguments[3];
        LocalDate start;
        LocalDate end;
        try {
            start = LocalDate.parse(startStr, DateTimeFormatter.ofPattern("yyyyMMdd"));
            end = LocalDate.parse(endStr, DateTimeFormatter.ofPattern("yyyyMMdd"));

            boolean validDuration = false;

            StringBuilder durationErrorMsg = new StringBuilder();
            if (!start.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
                isNotMonError(durationErrorMsg, who, startStr, endStr, start.getDayOfWeek());
                validDuration = true;
            }

            if (!end.getDayOfWeek().equals(DayOfWeek.FRIDAY) && !end.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                isNotFriOrSunError(durationErrorMsg, who, startStr, endStr, end.getDayOfWeek());
                validDuration = true;
            }

            if (validDuration) {
                SendMessage message = SendMessage.builder().chatId(ID).text(durationErrorMsg.toString()).build();
                TelegramUtils.sendMessage(absSender, message);
                return;
            }
        } catch (DateTimeParseException e) {
            registerError(absSender);
            return;
        }

        WeekManager weekManager = WeekManager.create(who, start, end);
        weekManagerRepository.saveAndFlush(weekManager);

        String startSerialize = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(start);
        String endSerialize = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(end);
        SendMessage message = SendMessage.builder()
                .chatId(ID)
                .text(who + "님 주간 담당자 (" + startSerialize + " ~ " + endSerialize + ") 등록을 완료했습니다.")
                .build();

        TelegramUtils.sendMessage(absSender, message);
    }

    public StringBuilder isNotMonError(StringBuilder sb, String who, String startStr, String endStr, DayOfWeek dayOfWeek) {
        return sb.append(who).append("님의 등록 (").append(startStr).append(" ").append(endStr).append("), 시작일은 월요일만 가능합니다. [").append(startStr).append("은 ").append(dayOfWeek.toString()).append(" 임]\n");
    }

    public StringBuilder isNotFriOrSunError(StringBuilder sb, String who, String startStr, String endStr, DayOfWeek dayOfWeek) {
        return sb.append(who).append("님의 등록 (").append(startStr).append(" ").append(endStr).append("), 종료일은 금요일 또는 일요일만 가능합니다. [").append(endStr).append("은 ").append(dayOfWeek.toString()).append(" 임]\n");
    }
    private void bulkRegister(AbsSender absSender, String[] arguments) {
        String[] pureArgs = Arrays.copyOfRange(arguments, 1, arguments.length);
        String pureArgsStr = String.join(" ", pureArgs);
        String[] bulkRegisterTargets = StringUtils.split(pureArgsStr, ",");

        List<WeekManager> saveTargets = new ArrayList<>();
        StringBuilder durationErrorMsg = new StringBuilder();
        for (String bulkRegisterTarget : bulkRegisterTargets) {
            String[] singleArguments = StringUtils.split(bulkRegisterTarget, " ");
            String who = singleArguments[0];
            String startStr = singleArguments[1];
            String endStr = singleArguments[2];

            LocalDate start;
            LocalDate end;

            try {
                start = LocalDate.parse(startStr, DateTimeFormatter.ofPattern("yyyyMMdd"));
                end = LocalDate.parse(endStr, DateTimeFormatter.ofPattern("yyyyMMdd"));

                boolean validDuration = false;

                if (!start.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
                    isNotMonError(durationErrorMsg, who, startStr, endStr, start.getDayOfWeek());
                    validDuration = true;
                }

                if (!end.getDayOfWeek().equals(DayOfWeek.FRIDAY) && !end.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                    isNotFriOrSunError(durationErrorMsg, who, startStr, endStr, end.getDayOfWeek());
                    validDuration = true;
                }

                if (validDuration) {
                    durationErrorMsg.append("\n");
                    continue;
                }
            } catch (DateTimeParseException e) {
                registerError(absSender);
                return;
            }
            WeekManager weekManager = WeekManager.create(who, start, end);
            saveTargets.add(weekManager);
        }

        if (!saveTargets.isEmpty()) {
            List<WeekManager> weekManagers = weekManagerRepository.saveAllAndFlush(saveTargets);

            StringBuilder sb = new StringBuilder();
            for (WeekManager weekManager : weekManagers) {
                String managerName = weekManager.getManagerName();
                String startSerialize = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(weekManager.getManageStart());
                String endSerialize = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(weekManager.getManageEnd());
                sb.append(managerName).append("님 ").append(startSerialize).append(" ~ ").append(endSerialize).append("\n");
            }

            sb.append("이상 위의 일괄 등록을 마쳤습니다.\n");
            if (!durationErrorMsg.toString().isEmpty()) {
                sb.append("------------------------\n")
                        .append("일괄 등록 중 아래의 등록은 실패했습니다.\n")
                        .append(durationErrorMsg).append("\n");
            }

            SendMessage message = SendMessage.builder()
                    .chatId(ID)
                    .text(sb.toString())
                    .build();
            TelegramUtils.sendMessage(absSender, message);
            return;
        } else {
            SendMessage message = SendMessage.builder()
                    .chatId(ID)
                    .text("일괄 등록을 시도했으나 알 수 없는 오류로 등록에 실패했습니다.")
                    .build();
            TelegramUtils.sendMessage(absSender, message);
            return;
        }
    }

    @Override
    public boolean isPublicCommand() {
        return false;
    }

    @Override
    public String getExtendedDescription() {
        return "!주번 등록 [담당자] [시작일] [종료일]\n" +
                "!주번 목록\n" +
                "!주번 삭제 [목록 ID]\n" +
                "!주번 일괄등록 [담당자] [시작일] [종료일],[담당자] [시작일] [종료일], ...\n" +
                "등록 명령의 시작일과 종료일의 형식은 yyyyMMdd 입니다.\n" +
                "삭제 명령은 !주번 목록 명령을 실행한 뒤 노출되는 ID 를 !주번 삭제 [목록 ID] 에 입력하세요.";
    }
}
