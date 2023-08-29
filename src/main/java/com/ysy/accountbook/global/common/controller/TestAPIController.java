package com.ysy.accountbook.global.common.controller;

import com.ysy.accountbook.global.common.dto.RequestDto;
import com.ysy.accountbook.global.common.dto.ResponseDto;
import com.ysy.accountbook.global.common.dto.State;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestAPIController {

    @GetMapping("/timeout")
    public void testTimeout(int time) throws InterruptedException {
        Thread.sleep(20000);
    }

    @GetMapping("/api")
    public ResponseDto<String> testApi(@RequestBody RequestDto requestDto) throws InterruptedException {
        log.debug("testApi 실행");

        return ResponseDto.<String>builder()
                          .timestamp(LocalDateTime.now().toString())
                          .state(State.success)
                          //.message(requestDto.message)
                          .data("TEST Value")
                          .build();
    }
}
