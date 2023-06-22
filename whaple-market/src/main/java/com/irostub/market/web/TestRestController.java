package com.irostub.market.web;

import com.irostub.market.utils.InitData;
import com.irostub.market.utils.TelegramAuthValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class TestRestController {
    private final TelegramAuthValidator telegramAuthValidator;
    @InitBinder("initData")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(telegramAuthValidator);
    }

    @PostMapping("/test/btn")
    public ResponseEntity<?> testBtn(@Validated @RequestBody InitData initData) {
        return ResponseEntity.ok().build();
    }
}
