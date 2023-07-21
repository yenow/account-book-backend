package com.ysy.accountbook.global.config.security.oauth;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.ysy.accountbook.global.common.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * • Provider와의 Authorization 과정에서 Authorization request를 cookie에 저장하기 위한 클래스<br><br>
 * • oauth2_auth_request 쿠키 : 해당 Authorizaion request의 고유 아이디를 담는다.<br><br>
 * • redirect_uri 쿠키 : 해당 Authorization request시 파라미터로 넘어온 redirect_uri를 담는다.
 * 이 쿠키는 나중에 applcation.yml의 authorizedRedirectUri와 일치하는지 확인시 사용된다.<br>
 * http://localhost:8080/oauth2/authorize/google?redirect_uri=http://localhost:3000/oauth2/redirect
 * <br><br>
 * • 인증 요청시 생성된 2개의 쿠키는 인증이 종료될 때 실행되는 successHandler와 failureHandler에서 제거된다.<br><br>
 * • 2개의 쿠키 유효시간은 180초로 유효시간 내에 인증요청을 다시하면 만들어졌던 쿠키를 다시 사용한다.<br><br>
 */
@SuppressWarnings("SpellCheckingInspection")
@Slf4j
@Component
public class CookieAuthorizationRequestRepository implements AuthorizationRequestRepository {
    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    private static final int COOKIE_EXPIRE_SECONDS = 180;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        log.debug("excute loadAuthorizationRequest()");
        return CookieUtil.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                         .map(cookie -> {
                             log.debug("cookie = {}", cookie);
                             OAuth2AuthorizationRequest deserialize = CookieUtil.deserialize(cookie, OAuth2AuthorizationRequest.class);
                             log.debug("OAuth2AuthorizationRequest cookie = {}", deserialize);
                             return deserialize;
                         })
                         .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        log.debug("excute saveAuthorizationRequest()");
        if (authorizationRequest == null) {
            CookieUtil.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
            CookieUtil.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
            return;
        }

        CookieUtil.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtil.serialize(authorizationRequest), COOKIE_EXPIRE_SECONDS);
        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);

        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            CookieUtil.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, COOKIE_EXPIRE_SECONDS);
        }

    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        return this.loadAuthorizationRequest(request);
    }

    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtil.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
    }
}