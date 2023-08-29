package com.ysy.accountbook.domain.login.controller;

import com.ysy.accountbook.domain.login.dto.LoginRequest;
import com.ysy.accountbook.domain.login.dto.LoginResponse;
import com.ysy.accountbook.domain.login.service.LoginService;
import com.ysy.accountbook.domain.user.repository.UserRepository;
import com.ysy.accountbook.global.common.dto.ResponseDto;
import com.ysy.accountbook.global.common.dto.State;
import com.ysy.accountbook.global.config.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginAPIController {

    final private LoginService loginService;
    final private UserRepository userRepository;
    final private SecurityUtil securityUtil;

    /**
     * 자동 로그인<br>
     * - accessToken 재발급<br>
     * - refreshToken 재발급<br>
     *
     * @return
     */
    @PostMapping("/autoLogin")
    public ResponseDto<LoginResponse> autoLogin() {
        String email = securityUtil.getCurrentUsername()
                                   .orElseThrow();

        LoginResponse loginResponse = loginService.autoLogin(email);
        log.debug("response body = {}", loginResponse);

        return ResponseDto.<LoginResponse>builder()
                          .timestamp(LocalDateTime.now().toString())
                          .state(State.success)
                          .message("")
                          .data(loginResponse)
                          .build();
    }

    /**
     * 로그인
     *
     * @param loginRequest
     * @return
     */
    @PostMapping("/login")
    public ResponseDto<LoginResponse> googleLogin(@RequestBody LoginRequest loginRequest) {
        log.debug("request body = {}", loginRequest);
        LoginResponse loginResponse = loginService.googleLogin(loginRequest);
        log.debug("response body = {}", loginResponse);

        return ResponseDto.<LoginResponse>builder()
                          .timestamp(LocalDateTime.now().toString())
                          .state(State.success)
                          .message("")
                          .data(loginResponse)
                          .build();
    }

    @PostMapping("/logout")
    public void logout() {
        loginService.logout();
    }

    @RequestMapping("/getJwtToken")
    public LoginResponse getJwtToken(@RequestParam String accessToken, @RequestParam String refreshToken) {
        log.debug("accessToken = {}, refreshToken = {}", accessToken, refreshToken);

        return LoginResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build();
    }
}
