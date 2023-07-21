package com.ysy.accountbook.domain.trade.controller;

import com.ysy.accountbook.domain.account.dto.AccountDto;
import com.ysy.accountbook.domain.trade.dto.TradeSaveRequest;
import com.ysy.accountbook.domain.trade.dto.TradeSaveResponse;
import com.ysy.accountbook.domain.trade.service.TradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/trade")
public class TradeController {

    final private TradeService tradeService;

    /**
     * 가계부 등록
     *
     * @param tradeSaveRequest
     * @return
     */
    @PostMapping("/save")
    public List<TradeSaveResponse> saveTrade(@RequestBody TradeSaveRequest tradeSaveRequest) {
        log.info("{}",tradeSaveRequest);
        tradeService.saveTrade(tradeSaveRequest);
        return null;
    }



}
