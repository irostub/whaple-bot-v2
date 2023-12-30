package com.irostub.standard.bot.gpt;

import lombok.Data;

@Data
public class GptResponse {
    private String id;
    private String object;
    private int created;
    private String model;
    private GptResponseChoice[] choices;
    private GptResponseUsage usage;
    private Object system_fingerprint;
}
