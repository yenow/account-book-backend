package com.ysy.accountbook.domain.card.entity;

import com.ysy.accountbook.domain.bank_account.entity.BankAccount;
import com.ysy.accountbook.domain.base_entity.BaseEntity;
import com.ysy.accountbook.domain.trade_detail.entity.TradeDetail;
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
public class Card extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;

    @Comment("카드타입(신용카드,체크카드)")
    @Enumerated(value = EnumType.STRING)
    @Column(length = 30, nullable = false)
    private CardType cardType;

    @Comment("카드번호")
    @Column(length = 100)
    private String cardNumber;
}
