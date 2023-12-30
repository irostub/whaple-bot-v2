package com.irostub.standard.bot.gpt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GptRequest {
    private String model;
    private List<GptMessage> messages = new ArrayList<>();
    private int temperature;
    private Integer max_tokens;
}
