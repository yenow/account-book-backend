package com.ysy.accountbook.domain.login.dto;

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
public class LoginRequest {
    @NotNull
    private String accessToken;
    @NotNull
    private String refreshToken;
    private String email;
    private String name;
    private String photoUrl;
}
