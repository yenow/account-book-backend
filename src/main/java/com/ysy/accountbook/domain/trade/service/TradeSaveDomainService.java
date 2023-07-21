package com.ysy.accountbook.domain.trade.service;

import com.ysy.accountbook.domain.account.entity.Account;
import com.ysy.accountbook.domain.account.exception.AccountNotFoundException;
import com.ysy.accountbook.domain.account.repository.AccountRepository;
import com.ysy.accountbook.domain.trade.entity.Trade;
import com.ysy.accountbook.domain.trade.entity.TradeType;
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


@Slf4j
@RequiredArgsConstructor
@Service
public class TradeSaveDomainService {
    private UserRepository userRepository;
    private TradeRepository tradeRepository;
    private TradeDetailRepository tradeDetailRepository;
    private AccountRepository accountRepository;


    /**
     * 지출 등록
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
    public Trade saveExpense(Long userId, String tradeDate, Long amount, Long expenseAccountId, Long assetAccountId, String content, String memo) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(UserNotFoundException::new);

        Account expenseAccount = accountRepository.findAccountByUserAndAccountId(user, expenseAccountId)
                                                  .orElseThrow(AccountNotFoundException::new);
        Account assetAccount = accountRepository.findAccountByUserAndAccountId(user, assetAccountId)
                                                .orElseThrow(AccountNotFoundException::new);

        Trade trade = tradeRepository.save(Trade.builder()
                                                .tradeDate(TradeDate.builder()
                                                                    .tradeDate(tradeDate)
                                                                    .build())
                                                .user(user)
                                                .content(content)
                                                .tradeType(TradeType.expense)
                                                .memo(memo)
                                                .build());

        // 차변 (비용 증가)
        TradeDetail debit = tradeDetailRepository.save(TradeDetail.builder()
                                                                  .trade(trade)
                                                                  .account(expenseAccount)
                                                                  .debitAndCredit(DebitAndCredit.debit)
                                                                  .amount(amount)
                                                                  .build());
        // 대변 (자산 감소)
        TradeDetail credit = tradeDetailRepository.save(TradeDetail.builder()
                                                                   .trade(trade)
                                                                   .account(assetAccount)
                                                                   .debitAndCredit(DebitAndCredit.credit)
                                                                   .amount(amount)
                                                                   .build());


        return trade;
    }

    /**
     * 수입 등록
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
    public Trade saveIncome(Long userId, String tradeDate, Long amount, Long incomeAccountId, Long assetAccountId, String content, String memo) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(UserNotFoundException::new);

        Account incomeAccount = accountRepository.findAccountByUserAndAccountId(user, incomeAccountId)
                                                 .orElseThrow(AccountNotFoundException::new);
        Account assetAccount = accountRepository.findAccountByUserAndAccountId(user, assetAccountId)
                                                .orElseThrow(AccountNotFoundException::new);


        Trade trade = tradeRepository.save(Trade.builder()
                                                .tradeDate(TradeDate.builder()
                                                                    .tradeDate(tradeDate)
                                                                    .build())
                                                .user(user)
                                                .content(content)
                                                .tradeType(TradeType.income)
                                                .memo(memo)
                                                .build());
        // 차변 (쟈산 증가)
        tradeDetailRepository.save(TradeDetail.builder()
                                              .trade(trade)
                                              .account(assetAccount)
                                              .debitAndCredit(DebitAndCredit.debit)
                                              .amount(amount)
                                              .build());
        // 대변 (수입 증가)
        tradeDetailRepository.save(TradeDetail.builder()
                                              .trade(trade)
                                              .account(incomeAccount)
                                              .debitAndCredit(DebitAndCredit.credit)
                                              .amount(amount)
                                              .build());

        return trade;
    }

    /**
     * 이체 등록
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
    public Trade saveTransfer(Long userId, String tradeDate, Long amount, Long depositAccountId, Long withdrawAccountId, String content,
                              String memo) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(UserNotFoundException::new);

        Account depositAssetAccount = accountRepository.findAccountByUserAndAccountId(user, depositAccountId)
                                                       .orElseThrow(AccountNotFoundException::new);
        Account withdrawAssetAccount = accountRepository.findAccountByUserAndAccountId(user, withdrawAccountId)
                                                        .orElseThrow(AccountNotFoundException::new);

        Trade trade = tradeRepository.save(Trade.builder()
                                                .tradeDate(TradeDate.builder()
                                                                    .tradeDate(tradeDate)
                                                                    .build())
                                                .user(user)
                                                .content(content)
                                                .tradeType(TradeType.transfer)
                                                .memo(memo)
                                                .build());

        // 차변 (입금 자산 증가)
        tradeDetailRepository.save(TradeDetail.builder()
                                              .trade(trade)
                                              .account(depositAssetAccount)
                                              .debitAndCredit(DebitAndCredit.debit)
                                              .amount(amount)
                                              .build());
        // 대변 (출금 자산 감소)
        tradeDetailRepository.save(TradeDetail.builder()
                                              .trade(trade)
                                              .account(withdrawAssetAccount)
                                              .debitAndCredit(DebitAndCredit.credit)
                                              .amount(amount)
                                              .build());

        return trade;
    }
}
