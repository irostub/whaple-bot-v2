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

@Slf4j
@RestController
@CrossOrigin("*")
public class TestRestController {
    public static String hmacAndHex(String secret, String data, String Algorithms) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {

        //1. SecretKeySpec 클래스를 사용한 키 생성
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes("utf-8"), Algorithms);

        //2. 지정된  MAC 알고리즘을 구현하는 Mac 객체를 작성합니다.
        Mac hasher = Mac.getInstance(Algorithms);

        //3. 키를 사용해 이 Mac 객체를 초기화
        hasher.init(secretKey);

        //3. 암호화 하려는 데이터의 바이트의 배열을 처리해 MAC 조작을 종료
        byte[] hash = hasher.doFinal(data.getBytes());

        //4. Hex Encode to String
        return Hex.encodeHexString(hash);
    }

    @PostMapping("/test/btn")
    public ResponseEntity<?> testBtn(@RequestBody Data body, @Value("${app.bot.token}") String token) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        log.info("token={}", body.getToken());
        log.info("hash={}", body.getHash());
        String s = hmacAndHex(token, "WebAppData", "HmacSHA256");
        if (hmacAndHex(body.getToken(), s, "HmacSHA256").equals(body.hash)) {
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
