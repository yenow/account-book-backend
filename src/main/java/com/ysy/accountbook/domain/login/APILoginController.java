package com.ysy.accountbook.domain.login;

import com.ysy.accountbook.domain.login.dto.LoginResponse;
import com.ysy.accountbook.domain.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class APILoginController {

    final private LoginService loginService;

    @PostMapping("/login")
    public void login() {

    }

    @PostMapping("/logout")
    public void logout() {

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
