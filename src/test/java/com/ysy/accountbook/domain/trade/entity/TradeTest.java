package com.ysy.accountbook.domain.trade.entity;

import org.junit.jupiter.api.Test;

class TradeTest {

    @Test
    void trade() {
        Trade trade = Trade.builder()
                .tradeId(1L)
                .tradeDate(new TradeDate("20230401"))
                .build();
    }
}