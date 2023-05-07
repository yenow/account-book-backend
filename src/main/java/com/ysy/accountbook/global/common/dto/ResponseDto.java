package com.ysy.accountbook.global.common.dto;


import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ResponseDto {
    public Boolean isSuccess;
    public String message;
    public Object body;
}
