package com.ysy.accountbook.domain.login.dto;

import com.ysy.accountbook.domain.user.dto.UserDto;
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
public class LoginResponse {
    @NotNull
    private String accessToken;
    @NotNull
    private String refreshToken;
    private UserDto user;
}
