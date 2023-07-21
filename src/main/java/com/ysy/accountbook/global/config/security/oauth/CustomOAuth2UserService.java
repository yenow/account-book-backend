package com.ysy.accountbook.global.config.security.oauth;


import com.ysy.accountbook.domain.user.entity.User;
import com.ysy.accountbook.domain.user.repository.UserRepository;
import com.ysy.accountbook.global.config.security.oauth.dto.CustomUserDetails;
import com.ysy.accountbook.global.config.security.oauth.dto.OAuthAttributes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistration.ProviderDetails.UserInfoEndpoint;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 * • loadUser() 를 오버라이드해서 OAuth2UserRequest에 있는 Access Token으로 유저정보를 얻는다.<br><br>
 * • 획득한 유저 정보를 process()에서 Java Model과 매핑하고 가입 되지 않은 경우와 이미 가입된 경우를 구분하여 프로세스를 진행한다.<br><br>
 * • 결과로 OAuth2User를 구현한 CustomUserDetails 객체를 생성한다.
 */
@SuppressWarnings("SpellCheckingInspection")
@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.debug("accessToken = {}", userRequest.getAccessToken().getTokenValue());
        log.debug("accessTokenType = {}", userRequest.getAccessToken().getTokenType());
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        log.debug("oAuth2User = {}", oAuth2User);

        //AuthProvider authProvider = AuthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        //OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(authProvider, oAuth2User.getAttributes());

        // 현재 로그인 진행중인 서비스를 구분하는 코드
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        UserInfoEndpoint userInfoEndpoint = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint();

        String userInfoUri =  userInfoEndpoint.getUri();
        // OAuth2 로그인 진행 시 키가 되는 필드값, Primary Key와 같은 의미
        String nameAttributeKey = userInfoEndpoint.getUserNameAttributeName();

        // OAuth2UserService를 통해 가져온 OAuth2User의 attirbute를 담을 클래스
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, nameAttributeKey, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);

        return CustomUserDetails.create(user, oAuth2User.getAttributes());
    }

    /**
     * 유저 정보 저장 및 업데이트
     *
     * @param attributes
     * @return
     */
    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                                  .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                                  .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}