package com.ysy.accountbook.global.common.dto;


import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ResponseDto<T> {
    public String timestamp;
    public Integer errorCode;
    public State state;
    public String message;
    public T data;
}
