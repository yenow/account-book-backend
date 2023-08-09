package com.ysy.accountbook.domain.trade.controller;

import com.ysy.accountbook.domain.trade.dto.*;
import com.ysy.accountbook.domain.trade.dto.request.ChartRequest;
import com.ysy.accountbook.domain.trade.dto.response.ChartResponse;
import com.ysy.accountbook.domain.trade.dto.response.TradeSaveResponse;
import com.ysy.accountbook.domain.trade.service.ChartService;
import com.ysy.accountbook.domain.trade.service.TradeService;
import com.ysy.accountbook.global.common.dto.ResponseDto;
import com.ysy.accountbook.global.common.dto.State;
import com.ysy.accountbook.global.config.security.CustomUserDetails;
import com.ysy.accountbook.global.config.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/trade")
public class TradeAPIController {

    final private TradeService tradeService;
    final private ChartService chartService;
    final private SecurityUtil securityUtil;

    /**
     * 가계부 등록
     *
     * @param tradeSaveRequest
     * @return
     */
    @PostMapping("/save")
    public ResponseDto<TradeSaveResponse> saveTrade(@RequestBody TradeSaveRequest tradeSaveRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("{}", tradeSaveRequest);
        String email = userDetails.getUsername();

        tradeSaveRequest.setTradeDate(tradeSaveRequest.getTradeDate().replace("-",""));

        TradeSaveResponse tradeSaveResponse = tradeService.saveTrade(tradeSaveRequest, email);
        return ResponseDto.<TradeSaveResponse>builder()
                          .timestamp(LocalDateTime.now()
                                                  .toString())
                          .state(State.success)
                          .message("")
                          .data(tradeSaveResponse)
                          .build();
    }

    /**
     * 사용자의 모든 거래 가져오기
     *
     * @param
     * @param ResponseDto
     * @return
     */
    @PostMapping("/findAllTradeOfUser")
    public ResponseDto<Map<String, List<TradeDto>>> findAllTradeOfUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();
        Map<String, List<TradeDto>> trades = tradeService.findAllTradeOfUser(email);

        return ResponseDto.<Map<String, List<TradeDto>>>builder()
                          .timestamp(LocalDateTime.now()
                                                  .toString())
                          .state(State.success)
                          .message("")
                          .data(trades)
                          .build();
    }

    /**
     * 차트 데이터 가져오기
     *
     * @param userDetails
     * @return
     */
    @PostMapping("/findChartData")
    public ResponseDto<ChartResponse> findChartData(@RequestBody ChartRequest chartRequest,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();

        ChartResponse chartData = chartService.findChartData(chartRequest, email);


        return ResponseDto.<ChartResponse>builder()
                          .timestamp(LocalDateTime.now()
                                                  .toString())
                          .state(State.success)
                          .message("")
                          .data(chartData)
                          .build();
    }
}
