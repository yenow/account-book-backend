package com.ysy.accountbook.domain.trade.service;

import com.ysy.accountbook.domain.trade.dto.TradeSaveRequest;
import com.ysy.accountbook.domain.trade.dto.TradeSaveResponse;
import com.ysy.accountbook.domain.trade.entity.Trade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Service;

@SuppressWarnings("DuplicatedCode")
@Slf4j
@Service
@RequiredArgsConstructor
public class TradeService {

    final private TradeSaveDomainService tradeSaveDomainService;
    final private DefaultConversionService conversion;

    /**
     * 가계부 등록
     *
     * @param tsr
     */
    public TradeSaveResponse saveTrade(TradeSaveRequest tsr) {

        Trade trade;

        switch (tsr.getTradeType()) {
            case expense:
                trade = tradeSaveDomainService.saveExpense(tsr.getUserId(), tsr.getTradeDate(), tsr.getAmount(), tsr.getIncomeOrExpenseAccountId(),
                        tsr.getAssetAccountId(), tsr.getContent(), tsr.getMemo()
                                                          );
                break;
            case income:
                trade = tradeSaveDomainService.saveIncome(tsr.getUserId(), tsr.getTradeDate(), tsr.getAmount(), tsr.getIncomeOrExpenseAccountId(),
                        tsr.getAssetAccountId(), tsr.getContent(), tsr.getMemo()
                                                         );
                break;
            case transfer:
                trade = tradeSaveDomainService.saveTransfer(tsr.getUserId(), tsr.getTradeDate(), tsr.getAmount(), tsr.getDepositAccountId(),
                        tsr.getWithdrawAccountId(), tsr.getContent(), tsr.getMemo()
                                                           );
                break;

            default:
                throw new RuntimeException();
        }


        return conversion.convert(trade, TradeSaveResponse.class);
    }
}
