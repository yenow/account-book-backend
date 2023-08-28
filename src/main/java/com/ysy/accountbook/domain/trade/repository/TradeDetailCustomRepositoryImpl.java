package com.ysy.accountbook.domain.trade.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ysy.accountbook.domain.account.entity.QAccount;
import com.ysy.accountbook.domain.trade.entity.QTrade;
import com.ysy.accountbook.domain.trade.entity.QTradeDate;
import com.ysy.accountbook.domain.trade.entity.QTradeDetail;
import com.ysy.accountbook.domain.trade.entity.TradeDetail;
import com.ysy.accountbook.domain.user.entity.QUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@SuppressWarnings("SpellCheckingInspection")
@RequiredArgsConstructor
@Repository
public class TradeDetailCustomRepositoryImpl implements TradeDetailCustomRepository {
    private final JPAQueryFactory jqf;
    private final QTrade trade = QTrade.trade;
    private final QTradeDate tradeDate = QTradeDate.tradeDate1;
    private final QUser user = QUser.user;
    private final QTradeDetail tradeDetail = QTradeDetail.tradeDetail;
    private final QAccount account = QAccount.account;
}
