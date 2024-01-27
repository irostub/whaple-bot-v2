package com.irostub.standard.bot.music;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.irostub.domain.entity.standard.Music;
import com.irostub.domain.repository.MusicRepository;
import com.irostub.standard.bot.DefaultBotCommand;
import com.irostub.standard.bot.IManCommand;
import com.irostub.standard.bot.utils.TelegramUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MusicCommand extends DefaultBotCommand implements IManCommand {
    private final YouTube youTube;
    private final MusicRepository musicRepository;

    public MusicCommand(YouTube youTube, MusicRepository musicRepository) {
        super("음악", "음악을 추천하거나 목록을 볼 수 있습니다. !음악 쳐보세요");
        this.youTube = youTube;
        this.musicRepository = musicRepository;
    }

    @Override
    public String getExtendedDescription() {
        return "!음악 [youtube link]\n" +
                "!음악 목록\n";
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, Integer messageId, String[] arguments) {
        if (arguments.length < 1) {
            return;
        }
        if ("목록".equals(arguments[0])) {
            Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdDate"));
            Page<Music> pagedMusicList = musicRepository.findAll(pageable);
            StringBuilder messageText = new StringBuilder();
            List<MessageEntity> messageEntities = new ArrayList<>();
            if (pagedMusicList.isEmpty()) {
                SendMessage message = SendMessage.builder()
                        .chatId(chat.getId())
                        .text("아직 등록된 음악이 하나도 없네요? 어디 한번 등록해 볼까요?\n\n" +
                                "!음악 [youtube url] 을 사용해보세요!")
                        .build();
                TelegramUtils.sendMessage(absSender, message);
                return;
            }

            if (pagedMusicList.getTotalPages() == 1) {
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
                SendMessage message = SendMessage.builder()
                        .chatId(chat.getId())
                        .entities(messageEntities)
                        .text(messageText.toString())
                        .disableWebPagePreview(true)
                        .build();
                TelegramUtils.sendMessage(absSender, message);
                return;
            }

            if (pagedMusicList.hasNext()) {
                List<InlineKeyboardButton> buttons = new ArrayList<>();
                InlineKeyboardButton nextButton = InlineKeyboardButton.builder()
                        .callbackData("music 1")
                        .text("다음 페이지")
                        .build();
                buttons.add(nextButton);

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
                SendMessage message = SendMessage.builder()
                        .chatId(chat.getId())
                        .entities(messageEntities)
                        .text(messageText.toString())
                        .replyMarkup(keyboard)
                        .disableWebPagePreview(true)
                        .build();

                TelegramUtils.sendMessage(absSender, message);
                return;
            }
        }

        String url = arguments[0];
        String videoId = parseVideoId(url);
        if (videoId == null || StringUtils.isEmpty(videoId)) {
            log.info("cannot found videoId, url={}", url);
            SendMessage message = SendMessage.builder()
                    .chatId(chat.getId())
                    .text("URL 에서 youtube 비디오 ID 를 찾지 못했습니다. 봇 제작자에게 문의해주세요.")
                    .build();
            TelegramUtils.sendMessage(absSender, message);
            return;
        }

        try {
            VideoListResponse result = youTube.videos()
                    .list("snippet")
                    .setId(videoId)
                    .execute();

            if (result.getItems().isEmpty()) {
                log.info("cannot found youtube result, result={}", result);
                SendMessage message = SendMessage.builder()
                        .chatId(chat.getId())
                        .text("Youtube 에서 해당 링크로 아무 것도 찾지 못했습니다. 봇 제작자에게 문의해주세요.")
                        .build();
                TelegramUtils.sendMessage(absSender, message);
                return;
            }

            //어차피 id 하나씩 조회할꺼라 검색 결과도 하나뿐
            Video video = result.getItems().get(0);
            String title = video.getSnippet().getTitle();
            String description = video.getSnippet().getDescription();
            if (title != null && title.length() > 59) {
                title = StringUtils.truncate(title, 56) + "...";
            }
            if (description != null && description.length() > 59) {
                description = StringUtils.truncate(description, 56) + "...";
                description = StringUtils.replace(description, "\n", "");
            }

            Boolean exists = musicRepository.existsByVideoId(videoId);
            if (exists) {
                SendMessage message = SendMessage.builder()
                        .chatId(chat.getId())
                        .text("요청한 [" + title + "]은 이미 목록에 저장되어있습니다..\n")
                        .build();
                TelegramUtils.sendMessage(absSender, message);
                return;
            }

            Music music = new Music(title, description, url, videoId);
            Music save = musicRepository.save(music);
            SendMessage message = SendMessage.builder()
                    .chatId(chat.getId())
                    .text("요청한 [" + title + "]이 목록에 저장되었습니다.")
                    .build();
            TelegramUtils.sendMessage(absSender, message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String parseVideoId(String url) {
        if (url.contains("youtu.be")) {
            String sub1 = StringUtils.substringAfterLast(url, "/");
            return StringUtils.substringBefore(sub1, "?");
        }

        if (url.contains("youtube.com")) {
            if (url.contains("/live")) {
                String sub1 = StringUtils.substringAfterLast(url, "/");
                return StringUtils.substringBefore(sub1, "?");
            }
            String sub1 = StringUtils.substringAfterLast(url, "?");
            String sub2 = StringUtils.substringAfterLast(sub1, "v=");
            return StringUtils.substringBefore(sub2, "&");
        }
        return null;
    }
}
