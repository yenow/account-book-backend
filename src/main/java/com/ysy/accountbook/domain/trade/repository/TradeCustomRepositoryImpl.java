package com.ysy.accountbook.domain.trade.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ysy.accountbook.domain.account.entity.AccountType;
import com.ysy.accountbook.domain.account.entity.QAccount;
import com.ysy.accountbook.domain.trade.dto.ChartDto;
import com.ysy.accountbook.domain.trade.dto.TradeDto;
import com.ysy.accountbook.domain.trade.entity.*;
import com.ysy.accountbook.domain.user.entity.QUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.querydsl.core.types.Ops.DIV;

@Slf4j
@SuppressWarnings("SpellCheckingInspection")
@RequiredArgsConstructor
@Repository
public class TradeCustomRepositoryImpl implements TradeCustomRepository {

    private final JPAQueryFactory jqf;
    private final QTrade trade = QTrade.trade;
    private final QTradeDate tradeDate = QTradeDate.tradeDate1;
    private final QUser user = QUser.user;
    private final QTradeDetail tradeDetail = QTradeDetail.tradeDetail;
    private final QAccount account = QAccount.account;


    /**
     * 사용자의 모든 거래 목록 조회
     *
     * @param userId
     */
    @Override
    public Optional<List<TradeDto>> findAllTradeByUserId(Long userId) {
        List<TradeDto> tradeDtoList = queryFindAllTradeByUser(userId);

        return Optional.of(tradeDtoList);
    }

    /**
     * 지출 차트 데이터 조회
     *
     * @param month       월(yyyyMM)
     * @param accountType 계정 분류(expense, income)
     * @param userId      사용자 아이디
     * @return
     */
    @Override
    public Optional<List<ChartDto>> findChartData(String month,
                                                  Long userId,
                                                  AccountType accountType) {

        if (accountType != AccountType.expense && accountType != AccountType.income) {
            throw new RuntimeException("AccountType이 올바르지 않습니다.");
        }

        Long sumAmount = queryFindSumAmount(month, userId, accountType);
        log.info("sumAmount = {}", sumAmount);
        if (sumAmount != null) {
            return Optional.ofNullable(queryFindChartDto(month, userId, accountType, sumAmount));
        }


        List<ChartDto> chartDtoList = new ArrayList<>();
        ChartDto chartDto = ChartDto.builder()
                                    .accountName("")
                                    .amount(0L)
                                    .percent(1.0F)
                                    .build();
        chartDtoList.add(chartDto);
        return Optional.ofNullable(chartDtoList);
    }

    /**
     * 차트 데이터 조회
     *
     * @param month
     * @param userId
     * @param accountType
     * @param sumAmount   금액 합계
     * @return
     */
    private List<ChartDto> queryFindChartDto(String month,
                                             Long userId,
                                             AccountType accountType,
                                             Long sumAmount) {

        return jqf.select(Projections.fields(ChartDto.class,
                                             tradeDate.tradeDate.substring(0, 6)
                                                                .as("month"),
                                             account.accountId,
                                             account.accountType,
                                             account.accountName,
                                             tradeDetail.amount.sum()
                                                               .as("amount"),
                                             tradeDetail.amount.sum()
                                                               .divide(sumAmount)
                                                               .floatValue()
                                                               .as("percent")))
                  .from(trade)
                  .innerJoin(trade.tradeDate, tradeDate)
                  .innerJoin(trade.user, user)
                  .innerJoin(trade.tradeDetails, tradeDetail)
                  .innerJoin(tradeDetail.account, account)
                  .where(user.userId.eq(userId)
                                    .and(tradeDate.tradeDate.startsWith(month))
                                    .and(account.accountType.eq(accountType))
                                    .and(tradeDetail.isDelete.eq(false)))
                  .groupBy(account.accountId)
                  .fetch();
    }

    /**
     * 월 지출/수입 합계 금액 구하는 쿼리
     *
     * @param month
     * @param userId
     * @param accountType
     * @return
     */
    private Long queryFindSumAmount(String month,
                                    Long userId,
                                    AccountType accountType) {

        return jqf.select(new CaseBuilder().when(tradeDetail.amount.isNotNull())
                                           .then(tradeDetail.amount)
                                           .otherwise(0L)
                                           .sum()
                                           .as("amount"))
                  .from(trade)
                  .innerJoin(trade.tradeDate, tradeDate)
                  .innerJoin(trade.user, user)
                  .innerJoin(trade.tradeDetails, tradeDetail)
                  .innerJoin(tradeDetail.account, account)
                  .where(user.userId.eq(userId)
                                    .and(tradeDate.tradeDate.startsWith(month))
                                    .and(account.accountType.eq(accountType))
                                    .and(trade.isDelete.eq(false)))
                  .fetchOne();
    }

    /**
     * 유저의 모든 거래 목록 조회하는 쿼리
     *
     * @param userId
     * @return
     */
    private List<TradeDto> queryFindAllTradeByUser(Long userId) {
        return jqf.select(Projections.fields(TradeDto.class,
                                             trade.tradeId,
                                             user.userId,
                                             tradeDate.tradeDate,
                                             trade.tradeType,
                                             Expressions.stringTemplate("convert_keyword({0}, {1})", trade.tradeType, "kr")
                                                        .as("typeName"),
                                             new CaseBuilder().when(tradeDetail.debitAndCredit.eq(DebitAndCredit.debit))
                                                              .then(tradeDetail.amount)
                                                              .otherwise(0L)
                                                              .sum()
                                                              .as("amount"),
                                             new CaseBuilder().when(account.accountType.eq(AccountType.income)
                                                                                       .or(account.accountType.eq(AccountType.expense)))
                                                              .then(account.accountId)
                                                              .otherwise((Expression<Long>) null)
                                                              .max()
                                                              .as("incomeOrExpenseAccountId"),
                                             new CaseBuilder().when(account.accountType.eq(AccountType.income)
                                                                                       .or(account.accountType.eq(AccountType.expense)))
                                                              .then(account.accountName)
                                                              .otherwise("")
                                                              .max()
                                                              .as("incomeOrExpenseAccountName"),
                                             new CaseBuilder().when(trade.tradeType.eq(TradeType.income)
                                                                                   .or(trade.tradeType.eq(TradeType.expense))
                                                                                   .and(account.accountType.eq(AccountType.asset)))
                                                              .then(account.accountId)
                                                              .otherwise((Expression<Long>) null)
                                                              .max()
                                                              .as("assetAccountId"),
                                             new CaseBuilder().when(trade.tradeType.eq(TradeType.income)
                                                                                   .or(trade.tradeType.eq(TradeType.expense))
                                                                                   .and(account.accountType.eq(AccountType.asset)))
                                                              .then(account.accountName)
                                                              .otherwise("")
                                                              .max()
                                                              .as("assetAccountName"),
                                             new CaseBuilder().when(trade.tradeType.eq(TradeType.transfer)
                                                                                   .and(account.accountType.eq(AccountType.asset)
                                                                                                           .or(tradeDetail.debitAndCredit.eq(
                                                                                                                   DebitAndCredit.debit))))
                                                              .then(account.accountId)
                                                              .otherwise((Expression<Long>) null)
                                                              .max()
                                                              .as("depositAccountId"),
                                             new CaseBuilder().when(trade.tradeType.eq(TradeType.transfer)
                                                                                   .and(account.accountType.eq(AccountType.asset)
                                                                                                           .or(tradeDetail.debitAndCredit.eq(
                                                                                                                   DebitAndCredit.debit))))
                                                              .then(account.accountName)
                                                              .otherwise("")
                                                              .max()
                                                              .as("depositAccountName"),
                                             new CaseBuilder().when(trade.tradeType.eq(TradeType.transfer)
                                                                                   .and(account.accountType.eq(AccountType.asset)
                                                                                                           .or(tradeDetail.debitAndCredit.eq(
                                                                                                                   DebitAndCredit.credit))))
                                                              .then(account.accountId)
                                                              .otherwise((Expression<Long>) null)
                                                              .max()
                                                              .as("withdrawAccountId"),
                                             new CaseBuilder().when(trade.tradeType.eq(TradeType.transfer)
                                                                                   .and(account.accountType.eq(AccountType.asset)
                                                                                                           .or(tradeDetail.debitAndCredit.eq(
                                                                                                                   DebitAndCredit.credit))))
                                                              .then(account.accountName)
                                                              .otherwise("")
                                                              .max()
                                                              .as("withdrawAccountName"),
                                             trade.content,
                                             trade.memo))
                  .from(trade)
                  .innerJoin(trade.tradeDate, tradeDate)
                  .innerJoin(trade.user, user)
                  .innerJoin(trade.tradeDetails, tradeDetail)
                  .innerJoin(tradeDetail.account, account)
                  .where(trade.user.userId.eq(userId)
                                          .and(trade.isDelete.eq(false))
                                          .and(tradeDetail.isDelete.eq(false)))
                  .groupBy(trade.tradeId)
                  .fetch();
    }
}
