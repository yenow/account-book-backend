package com.ysy.accountbook.domain.trade.repository;

import com.ysy.accountbook.domain.account.entity.AccountType;
import com.ysy.accountbook.domain.trade.dto.ChartDto;
import com.ysy.accountbook.domain.trade.dto.TradeDto;
import com.ysy.accountbook.domain.trade.entity.TradeType;

import java.util.List;
import java.util.Optional;

public interface TradeCustomRepository {
    Optional<List<TradeDto>> findAllTradeByUserId(Long userId);
    Optional<List<ChartDto>> findChartData(String month, Long userId, AccountType accountType);
}
