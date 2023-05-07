package com.ysy.accountbook.domain.trade.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TradeSaveRequest {
    @NotNull
    private String tradeDate;
    @NotNull
    private Long userId;
    @NotNull
    private String content;
    private List<DebitSaveRequest> debitSaveRequests;
    private List<CreditSaveRequest> creditSaveRequests;
}
