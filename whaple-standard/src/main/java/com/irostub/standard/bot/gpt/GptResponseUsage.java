package com.irostub.standard.bot.gpt;

import lombok.Data;

@Data
public class GptResponseUsage {
    private int prompt_tokens;
    private int completion_tokens;
    private int total_tokens;
}
