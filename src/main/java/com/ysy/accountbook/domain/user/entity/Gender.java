package com.ysy.accountbook.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;


@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum Gender {
    man, woman;

    @JsonCreator
    public static Gender from(String s) {
        return Gender.valueOf(s.toLowerCase());
    }
}
