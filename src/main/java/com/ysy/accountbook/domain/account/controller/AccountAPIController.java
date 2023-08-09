package com.ysy.accountbook.domain.account.controller;

import com.ysy.accountbook.domain.account.dto.*;
import com.ysy.accountbook.domain.account.dto.request.AccountRequest;
import com.ysy.accountbook.domain.account.dto.request.AccountSaveRequest;
import com.ysy.accountbook.domain.account.dto.response.AccountSaveResponse;
import com.ysy.accountbook.domain.account.service.AccountService;
import com.ysy.accountbook.domain.trade.dto.response.ChartResponse;
import com.ysy.accountbook.global.common.dto.ResponseDto;
import com.ysy.accountbook.global.common.dto.State;
import com.ysy.accountbook.global.config.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountAPIController {

    final private AccountService accountService;

    /**
     * 계정 타입별 리스트
     *
     * @param accountRequest
     * @return
     */
    @PostMapping("/AccountsByType")
    public ResponseDto<AccountsByTypeDto> findAccountOrUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();
        AccountsByTypeDto accountByUserAndAccountType = accountService.findAccountOrUser(email);
        log.info("{}", accountByUserAndAccountType);

        return ResponseDto.<AccountsByTypeDto>builder()
                          .timestamp(LocalDateTime.now()
                                                  .toString())
                          .state(State.success)
                          .message("")
                          .data(accountByUserAndAccountType)
                          .build();
    }

    /**
     * 자산 저장
     *
     * @param accountSaveRequest
     * @return
     */
    @PostMapping("/asset/save")
    public ResponseDto<AccountSaveResponse> saveAssetAccount(@RequestBody AccountSaveRequest accountSaveRequest,
                                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();
        AccountSaveResponse accountSaveResponse = accountService.saveAssetAccount(accountSaveRequest, email);
        return ResponseDto.<AccountSaveResponse>builder()
                          .timestamp(LocalDateTime.now()
                                                  .toString())
                          .state(State.success)
                          .message("")
                          .data(accountSaveResponse)
                          .build();
    }

    /**
     * 자산 계정별 보유 금액 리스트
     *
     * @param userDetails
     * @return
     */
    @PostMapping("/findAssetAmountList")
    public ResponseDto<List<AssetDto>> findAssetAmountList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();
        List<AssetDto> assetDtoList = accountService.findAssetAmountList(email);

        return ResponseDto.<List<AssetDto>>builder()
                          .timestamp(LocalDateTime.now()
                                                  .toString())
                          .state(State.success)
                          .message("")
                          .data(assetDtoList)
                          .build();
    }


    /**
     * 계정 리스트 (카테고리 리스트)
     *
     * @param accountRequest
     * @return
     */
    @PostMapping("/find")
    @Deprecated
    public ResponseDto<ChartResponse> findAccountsByUserId(@RequestBody AccountRequest accountRequest) {
        List<AccountDto> accountsByUserId = accountService.findAccountsByUserId(accountRequest);
        return ResponseDto.<ChartResponse>builder()
                          .timestamp(LocalDateTime.now()
                                                  .toString())
                          .state(State.success)
                          .message("")
                          .data(ChartResponse.builder()
                                             .build())
                          .build();
    }
}
