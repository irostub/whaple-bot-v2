package com.irostub.standard.bot.gpt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.irostub.standard.bot.DefaultBotCommand;
import com.irostub.standard.bot.IManCommand;
import com.irostub.standard.bot.config.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
@Slf4j
public class GptCommand extends DefaultBotCommand implements IManCommand {
    private final AppProperties appProperties;

    public GptCommand(AppProperties appProperties) {
        super("gpt", "AI 를 통해 질문에 답변합니다.");
        this.appProperties = appProperties;
    }

    @Override
    public String getExtendedDescription() {
        return "!gpt [질문]\n" +
                "\n" +
                "---예시---\n" +
                "!gpt 오늘은 날씨가 좋아. 뭘 해야 할까?";
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, Integer messageId, String[] arguments) {
        if (arguments.length < 1) {
            SendMessage message = SendMessage.builder()
                    .chatId(chat.getId())
                    .text("질문이 없는 것 같습니다..")
                    .build();
            sendMessage(absSender, message);
            return;
        }

        String prompt = String.join(COMMAND_PARAMETER_SEPARATOR, arguments);

        GptMessage gptSystem = new GptMessage();
        gptSystem.setRole("system");
        gptSystem.setContent("당신은 프로그래밍의 전문가입니다. " +
                "JAVA, GO, Javascript 등의 각종 언어에 능숙 합니다. " +
                "또한, React, Spring 과 같은 개발 프레임워크에 대해서도 잘 알고 있습니다. " +
                "당신은 Kubernetes 또한 매우 잘 알고 있습니다. " +
                "데이터 서치하고 이를 기반으로 사용자가 질문한 내용에 사실만 답변을 합니다." +
                "유저를 반드시 선생님 이라고 부르십시오. ");

        GptMessage gptUser = new GptMessage();
        gptUser.setRole("user");
        gptUser.setContent(prompt);

        ObjectMapper mapper = new ObjectMapper();
        GptRequest chatGptRequest = new GptRequest("gpt-3.5-turbo", List.of(gptSystem, gptUser), 1, null);
        String input;
        try {
            input = mapper.writeValueAsString(chatGptRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", appProperties.getChatgptToken())
                .POST(HttpRequest.BodyPublishers.ofString(input))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Throwable e) {
            log.error("http communication error, e=",e);
        }

        if (response.statusCode() == 200) {
            GptResponse chatGptResponse = null;
            try {
                chatGptResponse = mapper.readValue(response.body(), GptResponse.class);
            } catch (JsonProcessingException e) {
                log.error("parse error, e=",e);
            }
            String answer = chatGptResponse.getChoices()[chatGptResponse.getChoices().length-1].getMessage().getContent();

            if (!answer.isEmpty()) {
                SendMessage message = SendMessage.builder()
                        .chatId(chat.getId())
                        .text(answer)
                        .parseMode("Markdown")
                        .build();
                sendMessage(absSender, message);
            }
        } else {
            log.error("status code={}, error={}",response.statusCode(),response.body());
        }
    }
    private void sendMessage(AbsSender absSender, SendMessage send) {
        try {
            absSender.execute(send);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
