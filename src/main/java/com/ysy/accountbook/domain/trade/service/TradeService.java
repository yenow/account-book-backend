package com.ysy.accountbook.domain.trade.service;

import com.ysy.accountbook.domain.account.entity.AccountType;
import com.ysy.accountbook.domain.trade.dto.*;
import com.ysy.accountbook.domain.trade.dto.request.ChartRequest;
import com.ysy.accountbook.domain.trade.dto.response.ChartResponse;
import com.ysy.accountbook.domain.trade.dto.response.TradeSaveResponse;
import com.ysy.accountbook.domain.trade.entity.Trade;
import com.ysy.accountbook.domain.trade.entity.TradeType;
import com.ysy.accountbook.domain.trade.exception.TradeNotFoundException;
import com.ysy.accountbook.domain.trade.repository.TradeRepository;
import com.ysy.accountbook.domain.trade.domain_service.TradeSaveDomainService;
import com.ysy.accountbook.domain.user.entity.User;
import com.ysy.accountbook.domain.user.exception.UserNotFoundException;
import com.ysy.accountbook.domain.user.repository.UserRepository;
import com.ysy.accountbook.global.common.util.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeService {

    final private TradeSaveDomainService tradeSaveDomainService;
    final private TradeRepository tradeRepository;
    final private UserRepository userRepository;
    final private DefaultConversionService conversion;
    private final EntityManager entityManager;


    /**
     * 거래 등록
     *
     * @param tsr
     */
    @Transactional
    public TradeSaveResponse saveTrade(TradeSaveRequest tsr,
                                       String email) {
        User user = userRepository.findUserByEmailAndIsDelete(email, false)
                                  .orElseThrow(UserNotFoundException::new);

        Trade trade;
        switch (tsr.getTradeType()
                   .name()) {
            case "expense":
                trade = tradeSaveDomainService.saveExpense(tsr.getTradeId(),
                                                           user.getUserId(),
                                                           tsr.getTradeDate(),
                                                           tsr.getTradeType(),
                                                           tsr.getAmount(),
                                                           tsr.getIncomeOrExpenseAccountId(),
                                                           tsr.getAssetAccountId(),
                                                           tsr.getContent(),
                                                           tsr.getMemo());
                break;
            case "income":
                trade = tradeSaveDomainService.saveIncome(tsr.getTradeId(),
                                                          user.getUserId(),
                                                          tsr.getTradeDate(),
                                                          tsr.getTradeType(),
                                                          tsr.getAmount(),
                                                          tsr.getIncomeOrExpenseAccountId(),
                                                          tsr.getAssetAccountId(),
                                                          tsr.getContent(),
                                                          tsr.getMemo());
                break;
            case "transfer":
                trade = tradeSaveDomainService.saveTransfer(tsr.getTradeId(),
                                                            user.getUserId(),
                                                            tsr.getTradeDate(),
                                                            tsr.getTradeType(),
                                                            tsr.getAmount(),
                                                            tsr.getIncomeOrExpenseAccountId(),
                                                            tsr.getAssetAccountId(),
                                                            tsr.getContent(),
                                                            tsr.getMemo());
                break;
            default:
                throw new RuntimeException();
        }

        return conversion.convert(trade, TradeSaveResponse.class);
    }


    /**
     * 거래 삭제 (가계부 삭제)
     *
     * @param tradeId
     * @param email
     */
    @Transactional
    public void deleteTrade(Long tradeId,
                            String email) {
        User user = userRepository.findUserByEmailAndIsDelete(email, false)
                                  .orElseThrow(UserNotFoundException::new);

        Trade trade = tradeRepository.findByTradeIdAndUserAndIsDelete(tradeId, user, false)
                                     .orElseThrow(TradeNotFoundException::new);

        trade.deleteTrade();

    }

    /**
     * 사용자의 모든 거래 조회
     *
     * @param email
     * @param
     * @return
     */
    public Map<String, List<TradeDto>> findAllTradeOfUser(String email) {
        User user = userRepository.findUserByEmailAndIsDelete(email, false)
                                  .orElseThrow(UserNotFoundException::new);

        List<TradeDto> tradeDtoList = tradeRepository.findAllTradeByUserId(user.getUserId())
                                                     .orElseThrow();

        return convertTradeResponse(tradeDtoList);
    }

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
        log.info("{}", Utility.prettyToString(expenseChartData));
        List<ChartDto> incomeChartData = tradeRepository.findChartData(chartRequest.getMonth(), user.getUserId(), AccountType.income)
                                                        .orElseThrow();
        log.info("{}", Utility.prettyToString(expenseChartData));

        return ChartResponse.builder()
                            .expenseChartData(expenseChartData)
                            .incomeChartData(incomeChartData)
                            .build();
    }

    /**
     * TradeResponse 형식으로 변환
     *
     * @param tradeDtoList
     * @return
     */
    private Map<String, List<TradeDto>> convertTradeResponse(List<TradeDto> tradeDtoList) {
        Map<String, List<TradeDto>> trades = new HashMap<>();
        tradeDtoList.forEach(tradeDto -> {
            List<TradeDto> tempTradeDtoList = trades.get(tradeDto.getTradeDate());

            // 비어있으면
            if (tempTradeDtoList == null) {
                tempTradeDtoList = new ArrayList<>();
            }
            tempTradeDtoList.add(tradeDto);
            trades.put(tradeDto.getTradeDate(), tempTradeDtoList);
        });
        return trades;
    }
}
