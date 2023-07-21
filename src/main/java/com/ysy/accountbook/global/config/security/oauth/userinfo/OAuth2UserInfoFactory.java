package com.ysy.accountbook.global.config.security.oauth.userinfo;

import java.security.AuthProvider;
import java.util.Map;

import static org.springframework.security.config.oauth2.client.CommonOAuth2Provider.GOOGLE;

@SuppressWarnings({"ConstantConditions", "EqualsBetweenInconvertibleTypes"})
public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(AuthProvider authProvider, Map<String, Object> attributes) {
        if (GOOGLE.equals(authProvider)) {
            return new GoogleOAuth2UserInfo(attributes);
        }
        throw new IllegalArgumentException("Invalid Provider Type.");
    }
}
