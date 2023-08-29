package com.ysy.accountbook.domain.trade.advice;


import com.ysy.accountbook.domain.trade.exception.TradeNotFoundException;
import com.ysy.accountbook.domain.user.exception.UserNotFoundException;
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
public class TradeAPIControllerAdvice extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(TradeNotFoundException.class)
    public ResponseDto<String> tradeNotFoundExceptionHandler(TradeNotFoundException tradeNotFoundException) {
        tradeNotFoundException.printStackTrace();

        return ResponseDto.<String>builder()
                          .timestamp(LocalDateTime.now()
                                                  .toString())
                          .state(State.error)
                          .message("등록된 가계부를 찾을수 없습니다")
                          .data("")
                          .build();
    }
}
