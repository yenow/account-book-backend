package com.ysy.accountbook.domain.trade.entity;

import com.ysy.accountbook.domain.trade_date.entity.TradeDate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TradeTest {

    @Test
    void trade() {
        Trade trade = Trade.builder()
                .tradeId(1L)
                .tradeDate(new TradeDate("20230401"))
                .build();
    }
}