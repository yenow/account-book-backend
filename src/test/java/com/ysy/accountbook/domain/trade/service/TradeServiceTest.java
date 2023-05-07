package com.ysy.accountbook.domain.trade.service;

import com.ysy.accountbook.domain.account.entity.Account;
import com.ysy.accountbook.domain.account.repository.AccountRepository;
import com.ysy.accountbook.domain.trade.entity.Trade;
import com.ysy.accountbook.domain.trade.repository.TradeRepository;
import com.ysy.accountbook.domain.trade_detail.entity.DebitAndCredit;
import com.ysy.accountbook.domain.trade_detail.entity.TradeDetail;
import com.ysy.accountbook.domain.trade_detail.repository.TradeDetailRepository;
import com.ysy.accountbook.domain.user.entity.User;
import com.ysy.accountbook.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@Slf4j
@SpringBootTest
@ActiveProfiles("local")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TradeServiceTest {

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
        User user = userRepository.findById(1L).orElse(new User());

        Trade trade = new Trade();
        Account account = accountRepository.findAccountByAccountName("현금").orElseThrow();
        //
        //TradeDetail tradeDetail = new TradeDetail(
        //        trade, account, DebitAndCredit.credit, 100000L, ""
        //);
        //tradeDetailRepository.save(tradeDetail);
    }

//    void saveItem() {
//        Item item = new Item();
//        itemRepository.save()
//    }
}