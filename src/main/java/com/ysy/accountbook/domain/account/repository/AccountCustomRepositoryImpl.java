package com.ysy.accountbook.domain.account.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ysy.accountbook.domain.account.dto.AssetDto;
import com.ysy.accountbook.domain.account.entity.AccountType;
import com.ysy.accountbook.domain.account.entity.QAccount;
import com.ysy.accountbook.domain.trade.entity.QTrade;
import com.ysy.accountbook.domain.trade.entity.QTradeDate;
import com.ysy.accountbook.domain.trade.entity.QTradeDetail;
import com.ysy.accountbook.domain.user.entity.QUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AccountCustomRepositoryImpl implements AccountCustomRepository {
    private final JPAQueryFactory jqf;
    private final QAccount account = QAccount.account;
    private final QTrade trade = QTrade.trade;
    private final QTradeDate tradeDate = QTradeDate.tradeDate1;
    private final QUser user = QUser.user;
    private final QTradeDetail tradeDetail = QTradeDetail.tradeDetail;

}
