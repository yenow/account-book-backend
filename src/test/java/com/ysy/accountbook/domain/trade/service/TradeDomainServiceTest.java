package com.ysy.accountbook.domain.trade.service;

import com.ysy.accountbook.domain.trade.dto.CreditSaveRequest;
import com.ysy.accountbook.domain.trade.dto.DebitSaveRequest;
import com.ysy.accountbook.domain.trade.dto.TradeSaveRequest;
import com.ysy.accountbook.domain.user.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("local")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TradeDomainServiceTest {

    @Autowired
    private TradeDomainService tradeDomainService;

    @Test
    @Transactional
    void saveTrade() throws UserNotFoundException, AccountNotFoundException {
        List<DebitSaveRequest> debitSaveRequests = new ArrayList<>();
        debitSaveRequests.add(DebitSaveRequest.builder()
                                              .accountId(30L)
                                              .accountName("현금(지갑)")
                                              .amount(10000L)
                                              .memo("계좌이체")
                                              .build());

        List<CreditSaveRequest> creditSaveRequests = new ArrayList<>();
        creditSaveRequests.add(CreditSaveRequest.builder()
                                                .accountId(29L)
                                                .accountName("신한은행 예금")
                                                .amount(10000L)
                                                .memo("계좌이체")
                                                .build());

        TradeSaveRequest tradeSaveRequest = TradeSaveRequest.builder()
                                                            .tradeDate("20230401")
                                                            .userId(1L)
                                                            .debitSaveRequests(debitSaveRequests)
                                                            .creditSaveRequests(creditSaveRequests)
                                                            .content("계좌이체")
                                                            .build();

        tradeDomainService.saveTrade(tradeSaveRequest);

    }
}