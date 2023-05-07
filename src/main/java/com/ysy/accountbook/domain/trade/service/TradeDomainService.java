package com.ysy.accountbook.domain.trade.service;

import com.ysy.accountbook.domain.account.entity.Account;
import com.ysy.accountbook.domain.account.exception.AccountNotFoundException;
import com.ysy.accountbook.domain.account.repository.AccountRepository;
import com.ysy.accountbook.domain.trade.dto.CreditSaveRequest;
import com.ysy.accountbook.domain.trade.dto.DebitSaveRequest;
import com.ysy.accountbook.domain.trade.dto.TradeSaveRequest;
import com.ysy.accountbook.domain.trade.entity.Trade;
import com.ysy.accountbook.domain.trade.exception.NotEqualAmount;
import com.ysy.accountbook.domain.trade.repository.TradeRepository;
import com.ysy.accountbook.domain.trade_date.entity.TradeDate;
import com.ysy.accountbook.domain.trade_detail.entity.DebitAndCredit;
import com.ysy.accountbook.domain.trade_detail.entity.TradeDetail;
import com.ysy.accountbook.domain.trade_detail.repository.TradeDetailRepository;
import com.ysy.accountbook.domain.user.entity.User;
import com.ysy.accountbook.domain.user.exception.UserNotFoundException;
import com.ysy.accountbook.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class TradeDomainService {

    final private TradeRepository tradeRepository;
    final private TradeDetailRepository tradeDetailRepository;
    final private UserRepository userRepository;
    final private AccountRepository accountRepository;

    /**
     * 거래 등록
     *
     * @param tradeSaveRequest
     * @throws UserNotFoundException
     */
    @Transactional
    public void saveTrade(TradeSaveRequest tradeSaveRequest) {
        checkBeforeSaveTrade(tradeSaveRequest);

        User user = userRepository.findById(tradeSaveRequest.getUserId())
                                  .orElseThrow(UserNotFoundException::new);

        Trade trade = Trade.builder()
                           .tradeDate(new TradeDate(tradeSaveRequest.getTradeDate()))
                           .content(tradeSaveRequest.getContent())
                           .user(user)
                           .build();

        trade = tradeRepository.save(trade);

        for (DebitSaveRequest debitSaveRequest : tradeSaveRequest.getDebitSaveRequests()) {
            saveTradeDetail(user, debitSaveRequest.getAccountId(), trade, debitSaveRequest.getAmount(), debitSaveRequest.getMemo());
        }

        for (CreditSaveRequest creditSaveRequest : tradeSaveRequest.getCreditSaveRequests()) {
            saveTradeDetail(user, creditSaveRequest.getAccountId(), trade, creditSaveRequest.getAmount(), creditSaveRequest.getMemo());
        }

    }

    /**
     * 거래등록 전에 유효성검사
     *
     * @param tradeSaveRequest
     */
    private void checkBeforeSaveTrade(TradeSaveRequest tradeSaveRequest) {
        long debitSum = tradeSaveRequest.getDebitSaveRequests()
                                        .stream()
                                        .mapToLong(DebitSaveRequest::getAmount)
                                        .sum();

        long creditSum = tradeSaveRequest.getCreditSaveRequests()
                                         .stream()
                                         .mapToLong(CreditSaveRequest::getAmount)
                                         .sum();

        if (debitSum != creditSum) {
            throw new NotEqualAmount();
        }
    }

    /**
     * 거래 상세정보 저장하기
     *
     * @param accountId
     * @param trade
     * @param amount
     * @param memo
     */
    private void saveTradeDetail(User user, Long accountId, Trade trade, Long amount, String memo) {
        Account account = accountRepository.findAccountByUserAndAccountId(user, accountId)
                                           .orElseThrow(AccountNotFoundException::new);

        TradeDetail tradeDetail = TradeDetail.builder()
                                             .trade(trade)
                                             .account(account)
                                             .debitAndCredit(DebitAndCredit.debit)
                                             .amount(amount)
                                             .memo(memo)
                                             .build();

        tradeDetail = tradeDetailRepository.save(tradeDetail);
        log.info("tradeDetail : {} : ", tradeDetail);
    }


    public void saveExpense() {

    }

    public void saveRevenue() {

    }
}
