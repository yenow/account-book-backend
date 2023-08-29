package com.ysy.accountbook.domain.trade.dto;

import com.ysy.accountbook.domain.account.entity.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Pattern;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ChartDto {
    @Pattern(regexp = "^\\d{6}$", message = "날짜 형식 오류 (yyyyMM)")
    private String month;
    private Long accountId;
    private AccountType accountType;
    private String accountName;
    private Long amount;
    private Float percent;

}
