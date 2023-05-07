package com.ysy.accountbook.domain.trade_bank_acount.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class TradeBankPK implements Serializable {

    @Column(name = "trade_detail_id")
    private Long TradeDetailId;
    @Column(name = "bank_account_id")
    private Long BankAccountId;
}
