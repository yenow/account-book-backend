package com.ysy.accountbook.domain.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ysy.accountbook.domain.account.entity.Account;
import com.ysy.accountbook.domain.account.entity.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AccountsByTypeDto {
    private List<AccountDto> incomeAccounts;
    private List<AccountDto> expenseAccounts;
    private List<AccountDto> assetAccounts;
}
