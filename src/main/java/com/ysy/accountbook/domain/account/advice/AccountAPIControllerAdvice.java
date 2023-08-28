package com.ysy.accountbook.domain.account.advice;


import com.ysy.accountbook.domain.account.exception.AccountNotDeleteException;
import com.ysy.accountbook.domain.account.exception.AccountNotFoundException;
import com.ysy.accountbook.domain.trade.exception.TradeNotFoundException;
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
public class AccountAPIControllerAdvice extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(AccountNotDeleteException.class)
    public ResponseDto<String> accountNotDeleteExceptionHandler(AccountNotDeleteException accountNotDeleteException) {
        accountNotDeleteException.printStackTrace();

        return ResponseDto.<String>builder()
                          .timestamp(LocalDateTime.now()
                                                  .toString())
                          .state(State.error)
                          .message("삭제가 불가능한 계정입니다.")
                          .data("")
                          .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseDto<String> accountNotFoundExceptionHandler(AccountNotFoundException accountNotFoundException) {
        accountNotFoundException.printStackTrace();

        return ResponseDto.<String>builder()
                          .timestamp(LocalDateTime.now()
                                                  .toString())
                          .state(State.error)
                          .message("카테고리를 찾을수 없습니다.")
                          .data("")
                          .build();
    }
}
