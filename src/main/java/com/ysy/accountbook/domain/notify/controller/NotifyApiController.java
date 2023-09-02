package com.ysy.accountbook.domain.notify.controller;

import com.ysy.accountbook.domain.notify.dto.NotifyDto;
import com.ysy.accountbook.domain.notify.entity.Notify;
import com.ysy.accountbook.domain.notify.repository.NotifyRepository;
import com.ysy.accountbook.global.common.dto.ResponseDto;
import com.ysy.accountbook.global.common.dto.State;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notify")
public class NotifyApiController {

    final private NotifyRepository notifyRepository;

    @GetMapping("/findAll")
    public ResponseDto<List<NotifyDto>> findAllNotify() {
        List<Notify> notifyList = notifyRepository.findAll();
        List<NotifyDto> data = notifyList.stream()
                                         .map(NotifyDto::new)
                                         .sorted(Comparator.comparing(NotifyDto::getModificationDate)
                                                           .reversed())
                                         .collect(Collectors.toList());
        return ResponseDto.<List<NotifyDto>>builder()
                          .state(State.success)
                          .timestamp(LocalDateTime.now()
                                                  .toString())
                          .data(data)
                          .build();
    }
}
