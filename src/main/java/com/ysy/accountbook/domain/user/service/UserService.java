package com.ysy.accountbook.domain.user.service;

import com.ysy.accountbook.domain.user.dto.UserSaveRequestDto;
import com.ysy.accountbook.domain.user.dto.UserSaveResponseDto;
import com.ysy.accountbook.domain.user.entity.User;
import com.ysy.accountbook.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    final private UserRepository userRepository;

    @Transactional
    public UserSaveResponseDto saveUser(UserSaveRequestDto userSaveRequestDto) {
        User user = userRepository.save(userSaveRequestDto.toEntity());
        return UserSaveResponseDto.toDto(user);
    }
}
