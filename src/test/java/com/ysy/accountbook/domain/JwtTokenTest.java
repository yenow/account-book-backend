package com.ysy.accountbook.domain;


import com.ysy.accountbook.domain.user.entity.Role;
import com.ysy.accountbook.domain.user.entity.User;
import com.ysy.accountbook.domain.user.exception.UserNotFoundException;
import com.ysy.accountbook.domain.user.repository.UserRepository;
import com.ysy.accountbook.global.config.security.CustomUserDetails;
import com.ysy.accountbook.global.config.security.CustomUserDetailsService;
import com.ysy.accountbook.global.config.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;

@Slf4j
@SpringBootTest
@ActiveProfiles("local")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JwtTokenTest {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void createToken() {

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("phantom_ysy@naver.com");

        String accessToken = jwtTokenProvider.createAccessToken(new UsernamePasswordAuthenticationToken(userDetails,
                                                                                                        userDetails.getPassword(),
                                                                                                        userDetails.getAuthorities()));
        log.info("accessToken = {}",
                 accessToken);

    }
}
