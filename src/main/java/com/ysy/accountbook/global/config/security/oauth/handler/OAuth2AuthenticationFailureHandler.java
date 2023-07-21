package com.ysy.accountbook.global.config.security.oauth.handler;

import com.ysy.accountbook.global.common.util.CookieUtil;
import com.ysy.accountbook.global.config.security.oauth.CookieAuthorizationRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.ysy.accountbook.global.config.security.oauth.CookieAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

/**
 * • OAuth2 로그인 실패시 호출되는 Handler<br><br>
 * • 인증요청시 생성된 쿠키들을 삭제하고 error를 담아 보낸다<br><br>
 */
@SuppressWarnings("SpellCheckingInspection")
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final CookieAuthorizationRequestRepository authorizationRequestRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.debug("excute onAuthenticationFailure()");
        String targetUrl = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                                     .map(Cookie::getValue)
                                     .orElse("/");

        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                                        .queryParam("error", exception.getLocalizedMessage())
                                        .build().toUriString();

        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}