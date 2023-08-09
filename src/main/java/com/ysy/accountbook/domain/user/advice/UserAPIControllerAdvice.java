package com.ysy.accountbook.domain.user.advice;


import com.ysy.accountbook.domain.login.dto.LoginResponse;
import com.ysy.accountbook.domain.user.exception.UserNotFoundException;
import com.ysy.accountbook.global.common.dto.ResponseDto;
import com.ysy.accountbook.global.common.dto.State;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice(basePackages = {"com.ysy.accountbook.domain"})
public class UserAPIControllerAdvice extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseDto<String> userNotFoundExceptionHandler(UserNotFoundException userNotFoundException) {
        log.debug("userNotFoundExceptionHandler() 실행");

        return ResponseDto.<String>builder()
                          .timestamp(LocalDateTime.now()
                                                  .toString())
                          .state(State.error)
                          .message("사용자를 찾을 수 없습니다.")
                          .data("")
                          .build();
    }
}
