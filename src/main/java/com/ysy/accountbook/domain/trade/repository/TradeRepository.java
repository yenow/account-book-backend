package com.ysy.accountbook.domain.trade.repository;

import com.ysy.accountbook.domain.trade.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long> {

}
