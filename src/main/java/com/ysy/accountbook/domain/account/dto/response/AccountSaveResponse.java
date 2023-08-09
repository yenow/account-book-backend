package com.ysy.accountbook.domain.account.dto.response;

import com.ysy.accountbook.domain.account.entity.AssetType;
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
public class AccountSaveResponse {
    private Long userId;
    private Long accountId;
    private String accountName;
    private Long parentAccountId;
    private String parentAccountName;
    private Long amount;
    private Integer level;
    private AssetType assetType;
}
