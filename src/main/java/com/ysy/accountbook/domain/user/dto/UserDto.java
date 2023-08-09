package com.ysy.accountbook.domain.user.dto;

import com.ysy.accountbook.domain.user.entity.Gender;
import com.ysy.accountbook.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserDto {
    private Long userId;
    private String email;
    private String name;
    private String role;
    private String refreshToken;

    static public UserDto toDto(User user) {
        return UserDto.builder()
                      .userId(user.getUserId())
                      .name(user.getName())
                      .email(user.getEmail())
                      .role(user.getRole()
                                .getKey())
                      .build();
    }
}
