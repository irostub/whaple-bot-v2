package com.irostub.market.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin("*")
public class TestRestController {

    @PostMapping("/test/btn")
    public ResponseEntity<?> testBtn(@RequestBody Data body, @Value("${app.bot.token}") String token) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String[] split = body.getToken().split("&");
        String dataCheckString = Arrays.stream(split)
                .sorted()
                .collect(Collectors.joining("\n"));
        log.info("dataCheckString={}", dataCheckString);
        String hash = Arrays.stream(split).filter(s -> s.startsWith("hash")).findFirst().get();
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec("WebAppData".getBytes(), "HmacSHA256");
        hmacSha256.init(secret_key);
        byte[] firstKey = hmacSha256.doFinal(token.getBytes());

        Mac newMacSha256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key2 = new SecretKeySpec(firstKey, "HmacSHA256");
        newMacSha256.init(secret_key2);
        byte[] bytes = newMacSha256.doFinal(dataCheckString.getBytes());
        String hex = Hex.encodeHexString(bytes);
        log.info(hash);
        log.info(hex);
        if (hex.equals(hash.substring(5))) {
            log.info("success");
            return ResponseEntity.ok().build();
        }
        log.info("fail");
        return ResponseEntity.badRequest().build();
    }

    @lombok.Data
    static class Data{
        String userId;
        String token;
        String hash;
    }
}
