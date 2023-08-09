package com.ysy.accountbook.domain.trade.repository;

import com.ysy.accountbook.domain.account.entity.AccountType;
import com.ysy.accountbook.domain.trade.dto.ChartDto;
import com.ysy.accountbook.domain.trade.dto.TradeDto;
import com.ysy.accountbook.domain.user.repository.UserRepository;
import com.ysy.accountbook.global.common.util.Utility;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@Slf4j
@SpringBootTest
@ActiveProfiles("local")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TradeCustomRepositoryImplTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TradeRepository tradeRepository;


    @Test
    void findAllTradeByUser() {

        List<TradeDto> tradeDtoList = tradeRepository.findAllTradeByUserId(1L)
                                                     .orElseThrow();

        log.info("{}", Utility.prettyToString(tradeDtoList));
    }

    @Test
    void findChartData() {
        List<ChartDto> chartDtoList = tradeRepository.findChartData("202304", 1L, AccountType.expense)
                                                     .orElseThrow();
        log.info("{}", Utility.prettyToString(chartDtoList));
    }
}