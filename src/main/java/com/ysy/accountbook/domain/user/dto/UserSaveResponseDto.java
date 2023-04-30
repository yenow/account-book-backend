package com.ysy.accountbook.domain.user.dto;

import com.ysy.accountbook.domain.user.entity.Gender;
import com.ysy.accountbook.domain.user.entity.User;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSaveResponseDto {
    @NotNull
    private String email;
    @NotNull
    private String nickname;
    @NotNull
    private String birthDay;
    @NotNull
    private Gender gender;
    private String password;

    static public UserSaveResponseDto toDto(User user) {
        return UserSaveResponseDto.builder()
                .email(user.getEmail())
                .build();
    }
}
