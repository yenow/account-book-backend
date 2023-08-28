package com.ysy.accountbook.domain.trade.domain_service;

import com.ysy.accountbook.domain.account.entity.Account;
import com.ysy.accountbook.domain.account.exception.AccountNotFoundException;
import com.ysy.accountbook.domain.account.repository.AccountRepository;
import com.ysy.accountbook.domain.base.BaseEntity;
import com.ysy.accountbook.domain.trade.entity.Trade;
import com.ysy.accountbook.domain.trade.entity.TradeType;
import com.ysy.accountbook.domain.trade.exception.TradeNotFoundException;
import com.ysy.accountbook.domain.trade.repository.TradeRepository;
import com.ysy.accountbook.domain.trade.entity.TradeDate;
import com.ysy.accountbook.domain.trade.entity.DebitAndCredit;
import com.ysy.accountbook.domain.trade.entity.TradeDetail;
import com.ysy.accountbook.domain.trade.repository.TradeDetailRepository;
import com.ysy.accountbook.domain.user.entity.User;
import com.ysy.accountbook.domain.user.exception.UserNotFoundException;
import com.ysy.accountbook.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("DuplicatedCode")
@Slf4j
@Service
@RequiredArgsConstructor
public class TradeSaveDomainService {
    final private UserRepository userRepository;
    final private TradeRepository tradeRepository;
    final private TradeDetailRepository tradeDetailRepository;
    final private AccountRepository accountRepository;


    /**
     * 지출 등록 (가계부 등록)
     *
     * @param userId           사용자 ID
     * @param tradeDate        날짜
     * @param amount           금액
     * @param expenseAccountId 지출 계정 ID
     * @param assetAccountId   자산 계정 ID
     * @param content          내용
     * @param memo             메모
     * @return
     */
    public Trade saveExpense(Long tradeId,
                             Long userId,
                             String tradeDate,
                             TradeType tradeType,
                             Long amount,
                             Long expenseAccountId,
                             Long assetAccountId,
                             String content,
                             String memo) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(UserNotFoundException::new);

        // 신규 등록
        Trade trade = createTrade(tradeId, tradeDate, tradeType, content, memo, user);
        return saveExpenseTradeDetail(trade, user, amount, expenseAccountId, assetAccountId);
    }

    /**
     * 지출 데이터 생성 (tradeDetail)
     *
     * @param trade            거래
     * @param user             사용자
     * @param amount           금액
     * @param expenseAccountId 지출 계정 ID
     * @param assetAccountId   자산 계정 ID
     */
    private Trade saveExpenseTradeDetail(Trade trade,
                                         User user,
                                         Long amount,
                                         Long expenseAccountId,
                                         Long assetAccountId) {
        Account expenseAccount = accountRepository.findAccountByUserAndAccountId(user, expenseAccountId)
                                                  .orElseThrow(AccountNotFoundException::new);
        Account assetAccount = accountRepository.findAccountByUserAndAccountId(user, assetAccountId)
                                                .orElseThrow(AccountNotFoundException::new);


        // 차변 (비용 증가)
        TradeDetail debit = tradeDetailRepository.save(TradeDetail.builder()
                                                                  .trade(trade)
                                                                  .account(expenseAccount)
                                                                  .debitAndCredit(DebitAndCredit.debit)
                                                                  .amount(amount)
                                                                  .build());
        trade.getTradeDetails()
             .add(debit);

        // 대변 (자산 감소)
        TradeDetail credit = tradeDetailRepository.save(TradeDetail.builder()
                                                                   .trade(trade)
                                                                   .account(assetAccount)
                                                                   .debitAndCredit(DebitAndCredit.credit)
                                                                   .amount(amount)
                                                                   .build());
        trade.getTradeDetails()
             .add(credit);

        return trade;
    }

    /**
     * 수입 등록 (가계부 등록)
     *
     * @param userId          사용자 ID
     * @param tradeDate       날짜
     * @param amount          금액
     * @param incomeAccountId 수입 계정 ID
     * @param assetAccountId  자산 계정 ID
     * @param content         내용
     * @param memo            메모
     * @return
     */
    public Trade saveIncome(Long tradeId,
                            Long userId,
                            String tradeDate,
                            TradeType tradeType,
                            Long amount,
                            Long incomeAccountId,
                            Long assetAccountId,
                            String content,
                            String memo) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(UserNotFoundException::new);

        // 신규 등록
        Trade trade = createTrade(tradeId, tradeDate, tradeType, content, memo, user);
        return saveIncomeTradeDetail(trade, user, amount, incomeAccountId, assetAccountId);
    }

    /**
     * 수입 등록
     *
     * @param trade
     * @param user
     * @param amount
     * @param incomeAccountId 수입 계정과목
     * @param assetAccountId
     * @return
     */
    private Trade saveIncomeTradeDetail(Trade trade,
                                       User user,
                                       Long amount,
                                       Long incomeAccountId,
                                       Long assetAccountId) {

        Account incomeAccount = accountRepository.findAccountByUserAndAccountId(user, incomeAccountId)
                                                 .orElseThrow(AccountNotFoundException::new);
        Account assetAccount = accountRepository.findAccountByUserAndAccountId(user, assetAccountId)
                                                .orElseThrow(AccountNotFoundException::new);

        // 차변 (쟈산 증가)
        TradeDetail debit = tradeDetailRepository.save(TradeDetail.builder()
                                                                  .trade(trade)
                                                                  .account(assetAccount)
                                                                  .debitAndCredit(DebitAndCredit.debit)
                                                                  .amount(amount)
                                                                  .build());
        trade.getTradeDetails()
             .add(debit);

        // 대변 (수입 증가)
        TradeDetail credit = tradeDetailRepository.save(TradeDetail.builder()
                                                                   .trade(trade)
                                                                   .account(incomeAccount)
                                                                   .debitAndCredit(DebitAndCredit.credit)
                                                                   .amount(amount)
                                                                   .build());
        trade.getTradeDetails()
             .add(credit);

        return trade;
    }

    /**
     * 이체 등록 (가계부 등록)
     *
     * @param userId            사용자 ID
     * @param tradeDate         날짜
     * @param amount            금액
     * @param depositAccountId  입금 자산 계정 ID
     * @param withdrawAccountId 출금 자산 계정 ID
     * @param content           내용
     * @param memo              메모
     * @return
     */
    public Trade saveTransfer(Long tradeId,
                              Long userId,
                              String tradeDate,
                              TradeType tradeType,
                              Long amount,
                              Long depositAccountId,
                              Long withdrawAccountId,
                              String content,
                              String memo) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(UserNotFoundException::new);

        // 신규 등록
        Trade trade = createTrade(tradeId, tradeDate, tradeType, content, memo, user);
        return saveTransferTradeDetail(trade, user, amount, depositAccountId, withdrawAccountId);
    }

    /**
     * 이체 등록
     *
     * @param trade             거래
     * @param user              사용자
     * @param amount            금액
     * @param depositAccountId  입금 자산 계정 ID
     * @param withdrawAccountId 출금 자산 계정 ID
     * @return
     */
    private Trade saveTransferTradeDetail(Trade trade,
                                         User user,
                                         Long amount,
                                         Long depositAccountId,
                                         Long withdrawAccountId) {

        Account depositAssetAccount = accountRepository.findAccountByUserAndAccountId(user, depositAccountId)
                                                       .orElseThrow(AccountNotFoundException::new);
        Account withdrawAssetAccount = accountRepository.findAccountByUserAndAccountId(user, withdrawAccountId)
                                                        .orElseThrow(AccountNotFoundException::new);

        // 차변 (입금 자산 증가)
        TradeDetail debit = tradeDetailRepository.save(TradeDetail.builder()
                                                                  .trade(trade)
                                                                  .account(depositAssetAccount)
                                                                  .debitAndCredit(DebitAndCredit.debit)
                                                                  .amount(amount)
                                                                  .build());
        trade.getTradeDetails()
             .add(debit);

        // 대변 (출금 자산 감소)
        TradeDetail credit = tradeDetailRepository.save(TradeDetail.builder()
                                                                   .trade(trade)
                                                                   .account(withdrawAssetAccount)
                                                                   .debitAndCredit(DebitAndCredit.credit)
                                                                   .amount(amount)
                                                                   .build());
        trade.getTradeDetails()
             .add(credit);

        return trade;
    }

    private Trade createTrade(Long tradeId,
                              String tradeDate,
                              TradeType tradeType,
                              String content,
                              String memo,
                              User user) {
        Trade trade;
        if (tradeId == null) {
            trade = tradeRepository.save(Trade.builder()
                                              .tradeDate(TradeDate.builder()
                                                                  .tradeDate(tradeDate)
                                                                  .build())
                                              .user(user)
                                              .content(content)
                                              .tradeDetails(new ArrayList<>())
                                              .tradeType(tradeType)
                                              .memo(memo)
                                              .build());

        } else {    // 기존 거래 업데이트
            trade = tradeRepository.findByTradeIdAndIsDelete(tradeId, false)
                                   .orElseThrow(TradeNotFoundException::new);
            trade.updateTrade(tradeDate, tradeType, content, memo);

            // tradeDetail 초기화
            List<TradeDetail> tradeDetailList = tradeDetailRepository.findAllByTrade(trade);
            tradeDetailList.forEach(BaseEntity::delete);

        }
        return trade;
    }
}
