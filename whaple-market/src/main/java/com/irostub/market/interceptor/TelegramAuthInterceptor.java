package com.irostub.market.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
@Component
public class TelegramAuthInterceptor implements HandlerInterceptor {
    private final static String INIT_DATA_KEY = "initData";
    private final ObjectMapper ob;
    private final TelegramAuthValidator authValidator;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String initDataStr = request.getHeader(INIT_DATA_KEY);
        if (StringUtils.isBlank(initDataStr)) {
            try {
                InitData initData = ob.readValue(initDataStr, InitData.class);
                if (authValidator.validate(initData)) {
                    return true;
                }
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            } catch (TelegramAuthException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            } catch (Exception e) {
                log.error("request uri={}, error=", request.getRequestURI(), e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        }
        return false;
    }
}
