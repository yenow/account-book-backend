package com.ysy.accountbook.domain.account.entity;

import com.ysy.accountbook.domain.base.BaseEntity;
import com.ysy.accountbook.domain.user.entity.User;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;


/**
 * 계정 엔티티
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_account_id")
    private Account parentAccount;

    @Comment("계정타입(자산,부채,수익,비용,자본)")
    @Enumerated(value = EnumType.STRING)
    @Column(length = 10, nullable = false)
    @NonNull
    private AccountType accountType;
    @Comment("계정과목")
    @Column(length = 100, nullable = false)
    @NonNull
    private String accountName;
    @Comment("설명")
    @Column(length = 3000)
    private String description;
    @Comment("트리구조 깊이")
    @Column(length = 2)
    private Integer level;
    @Comment("leaf 여부")
    @Column(nullable = false, columnDefinition = "TINYINT", length = 1)
    private boolean isLeaf;
}
