package com.ysy.accountbook.domain.trade_acount.entity;

import com.ysy.accountbook.domain.bank_account.entity.BankAccount;
import com.ysy.accountbook.domain.base_entity.BaseEntity;
import com.ysy.accountbook.domain.common_code.entity.CommonCode;
import com.ysy.accountbook.domain.trade_detail.entity.TradeDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class TradeAccount extends BaseEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trade_detail_id")
    private TradeDetail tradeDetail;
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;
}
