package com.ysy.accountbook.domain.trade.service;

import com.ysy.accountbook.domain.account.entity.AccountType;
import com.ysy.accountbook.domain.trade.domain_service.TradeSaveDomainService;
import com.ysy.accountbook.domain.trade.dto.ChartDto;
import com.ysy.accountbook.domain.trade.dto.request.ChartRequest;
import com.ysy.accountbook.domain.trade.dto.response.ChartResponse;
import com.ysy.accountbook.domain.trade.entity.TradeType;
import com.ysy.accountbook.domain.trade.repository.TradeRepository;
import com.ysy.accountbook.domain.user.entity.User;
import com.ysy.accountbook.domain.user.exception.UserNotFoundException;
import com.ysy.accountbook.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChartService {

    final private TradeSaveDomainService tradeSaveDomainService;
    final private TradeRepository tradeRepository;
    final private UserRepository userRepository;
    final private DefaultConversionService conversion;

    /**
     * 월별 차트 데이터 검색
     *
     * @param chartRequest
     * @param email
     * @return
     */
    public ChartResponse findChartData(ChartRequest chartRequest,
                                       String email) {
        User user = userRepository.findUserByEmailAndIsDelete(email, false)
                                  .orElseThrow(UserNotFoundException::new);

        List<ChartDto> expenseChartData = tradeRepository.findChartData(chartRequest.getMonth(), user.getUserId(), AccountType.expense)
                                                         .orElseThrow();
        List<ChartDto> incomeChartData = tradeRepository.findChartData(chartRequest.getMonth(), user.getUserId(), AccountType.income)
                                                        .orElseThrow();

        return ChartResponse.builder()
                            .expenseChartData(expenseChartData)
                            .incomeChartData(incomeChartData)
                            .build();
    }
}
