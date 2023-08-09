package com.ysy.accountbook.global.config.security.oauth.handler;


import com.ysy.accountbook.global.common.util.CookieUtil;
import com.ysy.accountbook.global.config.security.jwt.JwtTokenProvider;
import com.ysy.accountbook.global.config.security.oauth.CookieAuthorizationRequestRepository;
import com.ysy.accountbook.global.config.security.oauth.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static com.ysy.accountbook.global.config.security.oauth.CookieAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

/**
 * - OAuth2 로그인 성공시 호출되는 Handler<br><br>
 * - 로그인에 성공하면 JWT를 생성한 다음 authorizedRedirectUri로 client에게 전송한다<br><br>
 * - redirectUri을 사용하는 이유는 oauth 로그인 성공시, 서버에는 jwt 토큰을 가지고 있지만 클라이언트는 토큰이 없기 때문에 redirect를 통해서 전달한다.<br>
 */
@SuppressWarnings("SpellCheckingInspection")
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${jwt.authorizedRedirectUri}")
    private String redirectUri;
    private final JwtTokenProvider tokenProvider;
    private final CookieAuthorizationRequestRepository authorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.debug("excute onAuthenticationSuccess()");
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            log.debug("Response has already been committed");
            return;
        }
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 쿠키에 redirectUrl이 있는지 확인
        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                                                 .map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException("redirect URIs are not matched");
        }
        String targetUrl = redirectUri.orElse("/login");

        // JWT 생성
        String jwtAccessToken = tokenProvider.createAccessToken(authentication);
        log.debug("create jwtAccessToken = {}, targetUrl = {}", jwtAccessToken, targetUrl);
        String jwtRefreshToken = tokenProvider.createRefreshToken(authentication);

        // URL 문자열 생성
        return UriComponentsBuilder.fromUriString(targetUrl)
                                   .queryParam("accessToken", jwtAccessToken)
                                   .queryParam("refreshToken", jwtRefreshToken)
                                   .build()
                                   .toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    /**
     * redirectUrl 비교
     *
     * @param uri
     * @return
     */
    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);
        URI authorizedUri = URI.create(redirectUri);
        log.debug("clientRedirectUri = {}, authorizedUri = {}", clientRedirectUri, authorizedUri);

        if (authorizedUri.getHost()
                         .equalsIgnoreCase(clientRedirectUri.getHost()) && authorizedUri.getPort() == clientRedirectUri.getPort()) {
            return true;
        }
        return false;
    }
}
