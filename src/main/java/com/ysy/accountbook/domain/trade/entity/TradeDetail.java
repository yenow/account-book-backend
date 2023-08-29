package com.ysy.accountbook.domain.trade.entity;

import com.ysy.accountbook.domain.account.entity.Account;
import com.ysy.accountbook.domain.base.BaseEntity;
import lombok.*;
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
@ToString(callSuper = true, exclude = {"trade","account"})
public class TradeDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tradeDetailId;

    @ManyToOne
    @JoinColumn(name = "trade_id")
    private Trade trade;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Comment("차변/대변")
    @Column(length = 10, columnDefinition = "varchar(10)", nullable = false)
    @Enumerated(EnumType.STRING)
    @NonNull
    private DebitAndCredit debitAndCredit;
    @Comment("금액")
    @Column(nullable = false)
    @NonNull
    private Long amount;

    public void deleteTradeDetail() {
        delete();
    }

}
