package com.ysy.accountbook.domain.trade.dto;

import com.ysy.accountbook.domain.trade.entity.TradeType;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TradeSaveRequest {
    private Long tradeId;
    @NonNull
    private Long userId;
    @NonNull
    private String tradeDate;
    @NonNull
    private TradeType tradeType;
    private String typeName;
    @NonNull
    private Long amount;

    private Long incomeOrExpenseAccountId;
    private String incomeOrExpenseAccountName;
    private Long assetAccountId;
    private String assetAccountName;
    private Long depositAccountId;
    private String depositAccountName;
    private Long withdrawAccountId;
    private String withdrawAccountName;
    private String content;
    private String memo;
}
