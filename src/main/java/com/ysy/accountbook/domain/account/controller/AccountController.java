package com.ysy.accountbook.domain.account.controller;

import com.ysy.accountbook.domain.account.dto.*;
import com.ysy.accountbook.domain.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    final private AccountService accountService;

    /**
     * 계정 리스트 (카테고리 리스트)
     *
     * @param accountRequest
     * @return
     */
    @PostMapping("/find")
    @Deprecated
    public List<AccountDto> findAccountsByUserId(@RequestBody AccountRequest accountRequest) {
        return accountService.findAccountsByUserId(accountRequest);
    }

    /**
     * 계정 타입별 리스트
     *
     * @param accountRequest
     * @return
     */
    @PostMapping("/AccountsByType")
    public AccountsByTypeDto findAccountByUserAndAccountType(@RequestBody AccountRequest accountRequest) {
        return accountService.findAccountByUserAndAccountType(accountRequest);
    }

    @PostMapping("/asset/save")
    public AccountSaveResponse saveAssetAccount(@RequestBody AccountSaveRequest accountSaveRequest) {
        AccountSaveResponse accountSaveResponse = accountService.saveAssetAccount(accountSaveRequest);
        return accountSaveResponse;
    }
}
