package com.irostub.standard.bot.gpt;

import lombok.Data;

@Data
public class GptResponseChoice {
    private int index;
    private GptMessage message;
    private Object logprobs;
    private String finish_reason;
}
