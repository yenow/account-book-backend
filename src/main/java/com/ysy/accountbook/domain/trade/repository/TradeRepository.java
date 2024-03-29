package com.ysy.accountbook.domain.trade.repository;

import com.ysy.accountbook.domain.trade.entity.Trade;
import com.ysy.accountbook.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TradeRepository extends JpaRepository<Trade, Long>, TradeCustomRepository {
    Optional<Trade> findByTradeIdAndIsDelete(Long tradeId,
                                             boolean isDelete);

    Optional<Trade> findByTradeIdAndUserAndIsDelete(Long tradeId,
                                                    User user,
                                                    boolean isDelete);
}
