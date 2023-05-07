package com.ysy.accountbook.domain.trade.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreditSaveRequest {
    @NotNull
    private Long accountId;
    @NotNull
    private String accountName;
    @NotNull
    private Long amount;
    private String memo;
}
