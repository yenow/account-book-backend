package com.ysy.accountbook.domain.trade_detail.entity;

import com.ysy.accountbook.domain.account.entity.Account;
import com.ysy.accountbook.domain.account.entity.AccountType;
import com.ysy.accountbook.domain.base_entity.BaseEntity;
import com.ysy.accountbook.domain.trade.entity.Trade;
import com.ysy.accountbook.domain.user.entity.User;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
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
    @Comment("적요(메모)")
    @Column(columnDefinition = "varchar(200) default ''")
    private String memo;
}
