package com.irostub.standard.bot.reactive;


import com.irostub.domain.entity.standard.Music;
import com.irostub.domain.repository.MusicRepository;
import com.irostub.standard.bot.utils.TelegramUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MusicCallbackAction {
    private final MusicRepository musicRepository;

    public void callback(AbsSender absSender, Update update) {
        String data = update.getCallbackQuery().getData();
        String[] splited = StringUtils.split(data, " ");
        String page = splited[1];
        int pageInteger = Integer.parseInt(page);
        Message message = update.getCallbackQuery().getMessage();

        StringBuilder messageText = new StringBuilder();
        List<MessageEntity> messageEntities = new ArrayList<>();

        Pageable pageable = PageRequest.of(pageInteger, 10, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Music> pagedMusicList = musicRepository.findAll(pageable);

        List<InlineKeyboardButton> buttons = new ArrayList<>();

        if (pagedMusicList.isFirst() && pagedMusicList.hasNext()) {
            InlineKeyboardButton nextButton = InlineKeyboardButton.builder()
                    .callbackData("music "+(pagedMusicList.getNumber()+1))
                    .text("다음 페이지")
                    .build();
            buttons.add(nextButton);
        }else if (pagedMusicList.isLast() && pagedMusicList.hasPrevious()){
            InlineKeyboardButton nextButton = InlineKeyboardButton.builder()
                    .callbackData("music "+(pagedMusicList.getNumber()-1))
                    .text("이전 페이지")
                    .build();
            buttons.add(nextButton);
        }else{
            InlineKeyboardButton prevButton = InlineKeyboardButton.builder()
                    .callbackData("music "+(pagedMusicList.getNumber()-1))
                    .text("이전 페이지")
                    .build();
            InlineKeyboardButton nextButton = InlineKeyboardButton.builder()
                    .callbackData("music "+(pagedMusicList.getNumber()+1))
                    .text("다음 페이지")
                    .build();
            buttons.add(prevButton);
            buttons.add(nextButton);
        }

        InlineKeyboardMarkup keyboard = InlineKeyboardMarkup.builder()
                .keyboardRow(buttons)
                .build();

        for (Music music : pagedMusicList) {
            String musicStr = "[" + music.getTitle() + "]\n";
            messageEntities.add(MessageEntity.builder()
                    .url(music.getUrl())
                    .type("text_link")
                    .offset(messageText.toString().length())
                    .length(musicStr.length())
                    .build());
            messageText.append(musicStr).append("\t").append(music.getDescription()).append("\n\n");
        }

        EditMessageText editMessage = EditMessageText.builder()
                .chatId(message.getChatId())
                .messageId(message.getMessageId())
                .text(messageText.toString())
                .entities(messageEntities)
                .replyMarkup(keyboard)
                .disableWebPagePreview(true)
                .build();

        TelegramUtils.editMessage(absSender, editMessage);
    }
}
