package com.ysy.accountbook.domain.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ysy.accountbook.domain.account.entity.Account;
import com.ysy.accountbook.domain.account.entity.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AccountDto {
    private Long accountId;
    private Long parentAccountId;
    private AccountType accountType;
    private String accountName;
    private String description;
    private Integer level;
    @JsonProperty(value="isLeaf")
    private boolean isLeaf;

    public AccountDto(Account account) {
        this.accountId = account.getAccountId();
        //this.parentAccountId = account.getParentAccount().getAccountId();
        this.accountType = account.getAccountType();
        this.accountName = account.getAccountName();
        this.description = account.getAccountName();
        this.level = account.getLevel();
        this.isLeaf = account.isLeaf();
    }
}
