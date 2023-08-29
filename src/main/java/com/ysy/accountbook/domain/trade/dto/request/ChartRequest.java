package com.ysy.accountbook.domain.trade.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Pattern;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ChartRequest {
    @NonNull
    @Pattern(regexp = "^\\d{6}$", message = "날짜 형식 오류 (yyyyMM)")
    private String month;
}
