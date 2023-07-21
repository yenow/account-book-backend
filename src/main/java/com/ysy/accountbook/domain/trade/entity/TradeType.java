package com.ysy.accountbook.domain.trade.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TradeType {
    income("수입"), expense("지출"), transfer("이체");

    private final String name;
}
