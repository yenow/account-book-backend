package com.ysy.accountbook.domain.account.controller;

import com.ysy.accountbook.domain.account.dto.*;
import com.ysy.accountbook.domain.account.dto.request.AccountRequest;
import com.ysy.accountbook.domain.account.dto.request.AccountSaveRequest;
import com.ysy.accountbook.domain.account.dto.response.AccountSaveResponse;
import com.ysy.accountbook.domain.account.service.AccountService;
import com.ysy.accountbook.domain.trade.dto.response.ChartResponse;
import com.ysy.accountbook.domain.user.entity.User;
import com.ysy.accountbook.domain.user.exception.UserNotFoundException;
import com.ysy.accountbook.domain.user.repository.UserRepository;
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
    final private UserRepository userRepository;

    /**
     * 계정 타입별 리스트
     *
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
     * 자산 저장
     *
     * @param accountSaveRequest
     * @return
     */
    @PostMapping("/save/asset")
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
     * 계정 저장
     *
     * @param accountSaveRequest
     * @return
     */
    @PostMapping("/save")
    public ResponseDto<AccountSaveResponse> saveAccount(@RequestBody AccountSaveRequest accountSaveRequest,
                                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userRepository.findUserByEmailAndIsDelete(email, false)
                                  .orElseThrow(UserNotFoundException::new);

        AccountSaveResponse accountSaveResponse = accountService.saveAccount(accountSaveRequest, user);
        return ResponseDto.<AccountSaveResponse>builder()
                          .timestamp(LocalDateTime.now()
                                                  .toString())
                          .state(State.success)
                          .message("")
                          .data(accountSaveResponse)
                          .build();
    }

    /**
     * 계정 삭제
     *
     * @param userDetails
     * @param accountId
     * @return
     */
    @PostMapping("/delete/{accountId}")
    public ResponseDto<AccountDto> deleteAccount(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                 @PathVariable Long accountId) {
        String email = userDetails.getUsername();
        User user = userRepository.findUserByEmailAndIsDelete(email, false)
                                  .orElseThrow(UserNotFoundException::new);

        AccountDto accountDto = accountService.deleteAccount(accountId, user);


        return ResponseDto.<AccountDto>builder()
                          .timestamp(LocalDateTime.now()
                                                  .toString())
                          .state(State.success)
                          .message("계정 삭제 성공")
                          .data(accountDto)
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
