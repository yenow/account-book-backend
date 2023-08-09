package com.ysy.accountbook.global.config.security;

import com.ysy.accountbook.domain.user.entity.Role;
import com.ysy.accountbook.global.config.security.jwt.JwtAccessDeniedHandler;
import com.ysy.accountbook.global.config.security.jwt.JwtAuthenticationEntryPoint;
import com.ysy.accountbook.global.config.security.jwt.JwtAuthenticationFilter;
import com.ysy.accountbook.global.config.security.oauth.CookieAuthorizationRequestRepository;
import com.ysy.accountbook.global.config.security.oauth.CustomOAuth2UserService;
import com.ysy.accountbook.global.config.security.oauth.handler.OAuth2AuthenticationFailureHandler;
import com.ysy.accountbook.global.config.security.oauth.handler.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers;

@SuppressWarnings({"unchecked", "SpellCheckingInspection"})
@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final OAuth2AuthenticationSuccessHandler authenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler authenticationFailureHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
            .disable()
            .headers()
            .frameOptions()
            .disable();

        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);    // session off

        // uri 권한 설정
        urlAuthSetting(http);

        // 필터 설정
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // oauth 설정
        // oAuthSetting(http);

        http.exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)  // 401
            .accessDeniedHandler(jwtAccessDeniedHandler);   // 403

        return http.build();
    }

    /**
     * URL 권한 설정
     *
     * @param http
     * @throws Exception
     */
    private void urlAuthSetting(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers(HttpMethod.GET)
                .permitAll()
            .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/login/**", "/test/**", "/autoLogin", "/logout", "/login")
                .permitAll()
            .antMatchers("/api/v1/**")
                .hasRole(Role.USER.name())
            .anyRequest()
                .authenticated();

    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, e) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter()
                    .write("ACCESS DENIED");
            response.getWriter()
                    .flush();
            response.getWriter()
                    .close();
        };
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, e) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter()
                    .write("UNAUTHORIZED");
            response.getWriter()
                    .flush();
            response.getWriter()
                    .close();
        };
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                           .antMatchers("/assets/**", "/h2-console/**", "/api/hello");
    }

    @Deprecated
    void oAuthSetting(HttpSecurity http) throws Exception {
        http.formLogin()
            .disable()
            .logout()
            .logoutSuccessUrl("/")
            .and()
            .oauth2Login()  // OAuth 2 로그인 기능에 대한 여러 설정의 진입점
            .authorizationEndpoint()    // 프론트엔드에서 백엔드로 소셜로그인 요청을 보내는 URI
            .baseUri("/oauth2/authorization/*") // Default URL= '/oauth2/authorization/{provider}'
            .authorizationRequestRepository(cookieAuthorizationRequestRepository)   // Authorization 과정에서 기본으로 Session을 사용하지만 Cookie로 변경하기 위해 설정
            .and()
            .redirectionEndpoint()  // Authorization 과정이 끝나면 Authorization Code와 함께 리다이렉트할 URI
            .baseUri("/login/oauth2/code/*")    // Default URL='/login/oauth2/code/{provider}'
            .and()
            .userInfoEndpoint() // OAuth 2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당
            .userService(customOAuth2UserService)  // 소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록
            .and()
            .successHandler(authenticationSuccessHandler)
            .failureHandler(authenticationFailureHandler);
    }
}
