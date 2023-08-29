package com.ysy.accountbook.global.common.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {
    //    private final ObjectMapper objectMapper;
    public static final String LOG_ID = "logId";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uuid = UUID.randomUUID()
                          .toString()
                          .substring(0, 10);
        request.setAttribute(LOG_ID, uuid);

        log.info("REQUEST URI : {}, METHOD : {}, UUID: {}", request.getRequestURI(), request.getMethod(), uuid);

        Long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Long startTime = (Long) request.getAttribute("startTime");
        String logId = (String) request.getAttribute(LOG_ID);
        Long stopTime = System.currentTimeMillis();
        Long elapsedTime = stopTime - startTime;
        log.info("RESPONSE URI : {}, RUN TIME : {}ms, UUID: {}, STATUS CODE: {}", request.getRequestURI(), elapsedTime, logId, response.getStatus());
    }
}
