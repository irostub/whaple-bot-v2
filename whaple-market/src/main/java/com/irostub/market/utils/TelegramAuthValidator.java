package com.irostub.market.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Component
public class TelegramAuthValidator implements Validator {
    private static final String ALGORITHM = "HmacSHA256";
    @Value("${app.bot.token}")
    private String botToken;

    private static byte[] HmacSHA256(byte[] secret, String data) {

        Mac hmacSha256 = null;
        try {
            hmacSha256 = Mac.getInstance(ALGORITHM);
            SecretKeySpec secretKey = new SecretKeySpec(secret, ALGORITHM);
            hmacSha256.init(secretKey);
            return hmacSha256.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            log.error("{} algorithm is not supported", ALGORITHM);
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            log.error("Unable to generate secret key.");
            throw new RuntimeException(e);
        }
    }

    private static byte[] HmacSHA256(String secret, String data) {
        return HmacSHA256(secret.getBytes(StandardCharsets.UTF_8), data);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return InitData.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        InitData initData = (InitData) target;
        byte[] secKey = HmacSHA256("WebAppData", botToken);
        byte[] byteHash = HmacSHA256(secKey, initData.getDataCheckString());
        if(!initData.getHash().equals(Hex.encodeHexString(byteHash))){
            errors.reject("valid.fail", "인증되지 않은 요청");
        }
    }
}
