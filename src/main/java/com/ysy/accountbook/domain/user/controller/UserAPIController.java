package com.ysy.accountbook.domain.user.controller;

import com.ysy.accountbook.domain.user.dto.UserSaveRequestDto;
import com.ysy.accountbook.domain.user.dto.UserSaveResponseDto;
import com.ysy.accountbook.domain.user.repository.UserRepository;
import com.ysy.accountbook.domain.user.service.UserService;
import com.ysy.accountbook.global.common.dto.ResponseDto;
import com.ysy.accountbook.global.common.dto.State;
import com.ysy.accountbook.global.config.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserAPIController {

    final private UserService userService;
    final private UserRepository userRepository;

    /**
     * 회원 등록 Api
     *
     * @param userSaveRequestDto
     */
    @PostMapping(value = "/save")
    public ResponseDto saveUser(@RequestBody UserSaveRequestDto userSaveRequestDto) {
        UserSaveResponseDto userSaveResponseDto = userService.saveUser(userSaveRequestDto);

        return ResponseDto.builder()
                .state(State.error)
                .data(userSaveResponseDto)
                .build();
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public UserSaveResponseDto getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();
        return null;
    }
}
