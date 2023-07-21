package com.ysy.accountbook.domain.trade.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("local")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TradeSaveDomainServiceTest {

    @Autowired
    private TradeSaveDomainService tradeSaveDomainService;

    @Test
    void saveExpense() {
    }

    @Test
    void saveIncome() {
    }

    @Test
    void saveTransfer() {
    }
}