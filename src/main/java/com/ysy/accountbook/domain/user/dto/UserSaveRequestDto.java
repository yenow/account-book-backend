package com.ysy.accountbook.domain.user.dto;

import com.ysy.accountbook.domain.user.entity.Gender;
import com.ysy.accountbook.domain.user.entity.User;
import javax.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserSaveRequestDto {
    @NotNull
    private String email;
    @NotNull
    private String nickname;
    @NotNull
    private String birthDay;
    @NotNull
    private Gender gender;
    private String password;

    public User toEntity() {
        return User.builder()
                .email(email)
                .build();
    }
}
