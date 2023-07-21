package com.ysy.accountbook.global.config.security;

import com.ysy.accountbook.domain.user.entity.User;
import com.ysy.accountbook.domain.user.exception.UserNotFoundException;
import com.ysy.accountbook.domain.user.repository.UserRepository;
import com.ysy.accountbook.global.config.security.oauth.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) {
        User user = userRepository.findById(Long.valueOf(username))
                                   .orElseThrow(UserNotFoundException::new);
        //User user = userRepository.findOneWithAuthoritiesByUsername(username)
        //                             .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));

        //if(!user.isActivated()) throw new RuntimeException(user.getUsername() + " -> 활성화되어 있지 않습니다.");
        return CustomUserDetails.create(user);
    }
}