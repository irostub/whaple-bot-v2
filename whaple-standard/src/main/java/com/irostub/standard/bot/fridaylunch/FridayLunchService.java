package com.irostub.standard.bot.fridaylunch;

import com.irostub.domain.entity.ChatGroup;
import com.irostub.domain.entity.FridayLunch;
import com.irostub.domain.repository.ChatGroupRepository;
import com.irostub.domain.repository.FridayLunchRepository;
import com.irostub.standard.bot.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Chat;

@RequiredArgsConstructor
@Service
public class FridayLunchService {
    private final FridayLunchRepository fridayLunchRepository;
    private final ChatGroupRepository chatGroupRepository;

    @Transactional
    public FridayLunch ChangeAlertState(Chat chat){
        FridayLunch fridayLunch = fridayLunchRepository.findByChatGroupId(chat.getId()).orElseGet(() -> {
            ChatGroup chatGroup = chatGroupRepository.findByChatGroupId(chat.getId()).orElseThrow(NotFoundException::new);
            FridayLunch newFridayLunch = FridayLunch.create(chatGroup);
            return fridayLunchRepository.save(newFridayLunch);
        });

        if (fridayLunch.isAlertOn()) {
            fridayLunch.alertOff();
        }else{
            fridayLunch.alertOn();
        }

        return fridayLunch;
    }
}
