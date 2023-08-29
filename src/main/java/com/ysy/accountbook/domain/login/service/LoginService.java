package com.ysy.accountbook.domain.login.service;

import com.ysy.accountbook.domain.account.repository.AccountRepository;
import com.ysy.accountbook.domain.login.dto.AutoLoginResponse;
import com.ysy.accountbook.domain.login.dto.LoginRequest;
import com.ysy.accountbook.domain.login.dto.LoginResponse;
import com.ysy.accountbook.domain.user.dto.UserDto;
import com.ysy.accountbook.domain.user.entity.Password;
import com.ysy.accountbook.domain.user.entity.Role;
import com.ysy.accountbook.domain.user.entity.User;
import com.ysy.accountbook.domain.user.exception.UserNotFoundException;
import com.ysy.accountbook.domain.user.repository.UserRepository;
import com.ysy.accountbook.global.common.util.CookieUtil;
import com.ysy.accountbook.global.config.security.SecurityUtil;
import com.ysy.accountbook.global.config.security.jwt.JwtTokenProvider;
import com.ysy.accountbook.global.config.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {
    @Value("${jwt.refresh-cookie-key}")
    private String cookieKey;
    final private JwtTokenProvider tokenProvider;
    final private AuthenticationManagerBuilder authenticationManagerBuilder;
    final private UserRepository userRepository;
    final private AccountRepository accountRepository;

    final private PasswordEncoder passwordEncoder;
    final private SecurityUtil securityUtil;

    /**
     * 자동 로그인
     *
     * @param email 이메일
     * @return
     */
    public LoginResponse autoLogin(String email) {
        User user = userRepository.findUserByEmailAndIsDelete(email, false)
                                  .orElseThrow(UserNotFoundException::new);

        Authentication authentication = getAuthentication(user.getEmail());

        // 인증 정보를 기준으로 jwt access 토큰 생성
        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        return LoginResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .user(UserDto.toDto(user))
                            .build();
    }

    /**
     * 구글 로그인
     *
     * @param loginRequest
     * @return LoginResponse
     */
    @Transactional
    public LoginResponse googleLogin(LoginRequest loginRequest) {
        Optional<User> oUser = userRepository.findUserByEmailAndIsDelete(loginRequest.getEmail(), false);

        if (oUser.isEmpty()) { // 신규 회원일 경우
            return googleSignUp(loginRequest);
        } else { // 기존 회원일 경우
            User user = oUser.get();

            Authentication authentication = getAuthentication(user.getEmail());

            // 인증 정보를 기준으로 jwt access 토큰 생성
            String accessToken = tokenProvider.createAccessToken(authentication);
            String refreshToken = tokenProvider.createRefreshToken(authentication);

            return LoginResponse.builder()
                                .user(UserDto.toDto(user))
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .build();
        }
    }


    /**
     * 신규 회원가입
     *
     * @param loginRequest
     * @return LoginResponse
     */
    private LoginResponse googleSignUp(LoginRequest loginRequest) {
        User user = userRepository.save(User.builder()
                                            .email(loginRequest.getEmail())
                                            .name(loginRequest.getName())
                                            .role(Role.USER)
                                            .isActivated(true)
                                            //.refreshToken(refreshToken)
                                            .build());

        Authentication authentication = getAuthentication(loginRequest.getEmail());

        // 인증 정보를 기준으로 jwt access 토큰 생성
        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        user.updateRefreshToken(refreshToken);

        // 기본 계정과목 초기화
        accountRepository.initAccount(user.getUserId());

        return LoginResponse.builder()
                            .user(UserDto.toDto(user))
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build();
    }

    private Authentication getAuthentication(String email) {
        // 받아온 유저네임과 패스워드를 이용해 UsernamePasswordAuthenticationToken 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, Password.PASSWORD);

        // authenticationToken 객체를 통해 Authentication 객체 생성
        // 이 과정에서 CustomUserDetailsService 에서 우리가 재정의한 loadUserByUsername 메서드 호출
        Authentication authentication = authenticationManagerBuilder.getObject()
                                                                    .authenticate(authenticationToken);
        log.debug("authentication = {}", authentication);

        return authentication;
    }

    public String refreshToken(HttpServletRequest request, HttpServletResponse response, String oldAccessToken) {
        // 1. Validation Refresh Token
        String oldRefreshToken = CookieUtil.getCookie(request, cookieKey)
                                           .map(Cookie::getValue)
                                           .orElseThrow(() -> new RuntimeException("no Refresh Token Cookie"));

        if (!tokenProvider.validateToken(oldRefreshToken)) {
            throw new RuntimeException("Not Validated Refresh Token");
        }

        // 2. 유저정보 얻기
        Authentication authentication = tokenProvider.getAuthentication(oldAccessToken);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        //Long id = userDetails.getUserId().orElseThrow();
        Long userId = 1L;

        // 3. Match Refresh Token
        String savedToken = userRepository.getRefreshTokenById(userId);

        if (!savedToken.equals(oldRefreshToken)) {
            throw new RuntimeException("Not Matched Refresh Token");
        }

        // 4. JWT 갱신
        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        return accessToken;
    }

    /**
     * 로그아웃<br>
     * - refreshToken 삭제
     */
    @Transactional
    public void logout() {
        String email = securityUtil.getCurrentUsername()
                                   .orElseThrow();

        User user = userRepository.findUserByEmail(email)
                                  .orElseThrow(UserNotFoundException::new);
        user.logout();
    }
}
