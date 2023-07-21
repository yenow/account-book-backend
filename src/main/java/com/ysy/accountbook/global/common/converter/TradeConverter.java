package com.ysy.accountbook.global.common.converter;

import com.ysy.accountbook.domain.trade.dto.TradeSaveResponse;
import com.ysy.accountbook.domain.trade.entity.DebitAndCredit;
import com.ysy.accountbook.domain.trade.entity.Trade;
import com.ysy.accountbook.domain.trade.entity.TradeDetail;
import com.ysy.accountbook.domain.trade.entity.TradeType;
import org.springframework.core.convert.converter.Converter;

import java.util.List;
import java.util.stream.Collectors;

public class TradeConverter implements Converter<Trade, TradeSaveResponse> {

    @Override
    public TradeSaveResponse convert(Trade trade) {

        List<TradeDetail> trades = trade.getTradeDetails();

        TradeDetail debit = trades.stream()
                                  .filter(tradeDetail -> tradeDetail.getDebitAndCredit() == DebitAndCredit.debit)
                                  .collect(Collectors.toList())
                                  .get(0);
        TradeDetail credit = trades.stream()
                                   .filter(tradeDetail -> tradeDetail.getDebitAndCredit() == DebitAndCredit.credit)
                                   .collect(Collectors.toList())
                                   .get(0);

        Long incomeOrExpenseAccountId = null;
        String incomeOrExpenseAccountName = "";
        Long assetAccountId = null;
        String assetAccountName = "";
        Long depositAccountId = null;
        String depositAccountName = "";
        Long withdrawAccountId = null;
        String withdrawAccountName = "";


        switch (trade.getTradeType()) {
            case income:
                incomeOrExpenseAccountId = credit.getAccount()
                                                 .getAccountId();
                incomeOrExpenseAccountName = credit.getAccount()
                                                   .getAccountName();
                assetAccountId = debit.getAccount()
                                      .getAccountId();
                assetAccountName = debit.getAccount()
                                        .getAccountName();
                break;
            case expense:
                incomeOrExpenseAccountId = debit.getAccount()
                                                .getAccountId();
                incomeOrExpenseAccountName = debit.getAccount()
                                                  .getAccountName();
                assetAccountId = credit.getAccount()
                                       .getAccountId();
                assetAccountName = credit.getAccount()
                                         .getAccountName();
            case transfer:
                depositAccountId = debit.getAccount()
                                        .getAccountId();
                depositAccountName = debit.getAccount()
                                          .getAccountName();
                withdrawAccountId = credit.getAccount()
                                          .getAccountId();
                withdrawAccountName = credit.getAccount()
                                            .getAccountName();
                break;
            default:
                throw new RuntimeException();
        }

        return TradeSaveResponse.builder()
                                .tradeId(trade.getTradeId())
                                .userId(trade.getUser()
                                             .getUserId())
                                .tradeDate(trade.getTradeDate()
                                                .getTradeDate())
                                .tradeType(trade.getTradeType())
                                .incomeOrExpenseAccountId(incomeOrExpenseAccountId)
                                .incomeOrExpenseAccountName(incomeOrExpenseAccountName)
                                .assetAccountId(assetAccountId)
                                .assetAccountName(assetAccountName)
                                .depositAccountId(depositAccountId)
                                .depositAccountName(depositAccountName)
                                .withdrawAccountId(withdrawAccountId)
                                .withdrawAccountName(withdrawAccountName)
                                .content(trade.getContent())
                                .memo(trade.getMemo())
                                .build();
    }
}
