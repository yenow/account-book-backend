package com.ysy.accountbook.global.common.dto;


import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDto {
    public Boolean isSuccess;
    public String message;
    public Object body;
}
