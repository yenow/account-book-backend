package com.ysy.accountbook.domain.trade.service;

import com.ysy.accountbook.domain.trade.converter.TradeConverter;
import com.ysy.accountbook.domain.trade.dto.*;
import com.ysy.accountbook.domain.trade.dto.response.TradeSaveResponse;
import com.ysy.accountbook.domain.trade.entity.Trade;
import com.ysy.accountbook.domain.trade.repository.TradeRepository;
import com.ysy.accountbook.domain.trade.domain_service.TradeSaveDomainService;
import com.ysy.accountbook.domain.user.entity.User;
import com.ysy.accountbook.domain.user.exception.UserNotFoundException;
import com.ysy.accountbook.domain.user.repository.UserRepository;
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
     * 가계부 등록
     *
     * @param tsr
     */
    @Transactional
    public TradeSaveResponse saveTrade(TradeSaveRequest tsr,
                                       String email) {
        User user = userRepository.findUserByEmailAndIsDelete(email, false)
                                  .orElseThrow(UserNotFoundException::new);

        Trade trade = null;

        switch (tsr.getTradeType()) {
            case expense:
                trade = tradeSaveDomainService.saveExpense(user.getUserId(),
                                                           tsr.getTradeDate(),
                                                           tsr.getAmount(),
                                                           tsr.getIncomeOrExpenseAccountId(),
                                                           tsr.getAssetAccountId(),
                                                           tsr.getContent(),
                                                           tsr.getMemo());
                break;
            case income:
                trade = tradeSaveDomainService.saveIncome(user.getUserId(),
                                                          tsr.getTradeDate(),
                                                          tsr.getAmount(),
                                                          tsr.getIncomeOrExpenseAccountId(),
                                                          tsr.getAssetAccountId(),
                                                          tsr.getContent(),
                                                          tsr.getMemo());
                break;
            case transfer:
                trade = tradeSaveDomainService.saveTransfer(user.getUserId(),
                                                            tsr.getTradeDate(),
                                                            tsr.getAmount(),
                                                            tsr.getDepositAccountId(),
                                                            tsr.getWithdrawAccountId(),
                                                            tsr.getContent(),
                                                            tsr.getMemo());
                break;

            default:
                throw new RuntimeException();
        }

        log.info("영속성 컨텍스트 생존 여부 : {}", entityManager.contains(trade));
        return conversion.convert(trade, TradeSaveResponse.class);
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

        Map<String, List<TradeDto>> trades = convertTradeResponse(tradeDtoList);

        return trades;
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
