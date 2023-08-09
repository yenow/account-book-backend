package com.ysy.accountbook.global.common.advice;


import com.ysy.accountbook.global.common.dto.ResponseDto;
import com.ysy.accountbook.global.common.dto.State;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice(basePackages = {"com.ysy.accountbook.domain"})
public class CommonApiControllerAdvice extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ResponseDto<String> runtimeExceptionHandler(RuntimeException runtimeException) {
        log.debug("runtimeExceptionHandler() 실행");
        log.debug("{}",
                  runtimeException.getMessage());
        runtimeException.printStackTrace();

        return ResponseDto.<String>builder()
                          .timestamp(LocalDateTime.now()
                                                  .toString())
                          .state(State.error)
                          .message("알 수 없는 에러가 발생했습니다.")
                          .data("")
                          .build();
    }
}
