package com.ysy.accountbook.domain.user.controller;

import com.ysy.accountbook.domain.user.dto.UserSaveRequestDto;
import com.ysy.accountbook.domain.user.dto.UserSaveResponseDto;
import com.ysy.accountbook.domain.user.service.UserService;
import com.ysy.accountbook.global.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserApiController {

    final private UserService userService;


    /**
     * 회원 등록 Api
     *
     * @param userSaveRequestDto
     */
    @RequestMapping(value = "/user/save", method = RequestMethod.POST)
    public ResponseDto saveUser(@RequestBody UserSaveRequestDto userSaveRequestDto) {
        UserSaveResponseDto userSaveResponseDto = userService.saveUser(userSaveRequestDto);

        return ResponseDto.builder()
                .isSuccess(true)
                .body(userSaveResponseDto)
                .build();
    }

}
