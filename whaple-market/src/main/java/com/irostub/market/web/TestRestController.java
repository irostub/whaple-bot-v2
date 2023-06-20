package com.irostub.market.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin("*")
public class TestRestController {
    @PostMapping("/test/btn")
    public ResponseEntity<?> testBtn(@RequestBody String body) {
        log.info(body);
        return ResponseEntity.ok().build();
    }
}
