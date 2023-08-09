package com.ysy.accountbook.domain.account.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AssetDto {
    private Long accountId;
    private Long parentAccountId;
    private String accountName;
    private String accountNamePath;
    private Long sumAmount;
    private Long level;
    private String assetType;
}
