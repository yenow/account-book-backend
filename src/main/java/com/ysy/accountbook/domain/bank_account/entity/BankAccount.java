package com.ysy.accountbook.domain.bank_account.entity;

import com.ysy.accountbook.domain.base_entity.BaseEntity;
import com.ysy.accountbook.domain.common_code.entity.CommonCode;
import com.ysy.accountbook.domain.trade_detail.entity.TradeDetail;
import com.ysy.accountbook.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
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
public class BankAccount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bankAccountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trade_detail_id")
    private TradeDetail tradeDetail;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_code")
    private CommonCode bankCode;

    @Comment("계좌번호")
    @Column(length = 100, nullable = false)
    private String bankAccountNumber;
}
