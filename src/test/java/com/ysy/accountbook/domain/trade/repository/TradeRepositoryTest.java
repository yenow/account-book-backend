package com.ysy.accountbook.domain.trade.repository;

import com.ysy.accountbook.domain.account.repository.AccountRepository;
import com.ysy.accountbook.domain.trade.entity.Trade;
import com.ysy.accountbook.domain.trade.entity.TradeDate;
import com.ysy.accountbook.domain.user.entity.User;
import com.ysy.accountbook.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest
@ActiveProfiles("local")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TradeRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TradeRepository tradeRepository;
    @Autowired
    private TradeDetailRepository tradeDetailRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Test
    void registerTrade() {
//        Item item = accountRepository.findItemByItemName("현금").orElseThrow();
        User user = userRepository.findUserByEmail("test@google.com").orElseThrow();
        TradeDate tradeDate = new TradeDate("20230303");

        Trade trade = new Trade();
        tradeRepository.save(trade);


    }
}