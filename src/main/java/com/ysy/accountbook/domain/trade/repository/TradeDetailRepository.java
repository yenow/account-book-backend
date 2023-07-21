package com.ysy.accountbook.domain.trade.repository;


import com.ysy.accountbook.domain.trade.entity.TradeDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeDetailRepository extends JpaRepository<TradeDetail, Long> {

    List<TradeDetail> findAllByAccount_AccountId(Long account_accountId);
}
