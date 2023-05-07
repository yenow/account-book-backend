package com.ysy.accountbook.domain.account.entity;

import com.ysy.accountbook.domain.base_entity.BaseEntity;
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
    private Account parentAccountId;

    @Comment("계정타입(자산,부채,수익,비용,자본)")
    @Enumerated(value = EnumType.STRING)
    @Column(length = 8, nullable = false)
    @NonNull
    private AccountType accountType;    // 수정불가
    @Comment("계정과목")
    @Column(length = 100, nullable = false)
    @NonNull
    private String accountName;
    @Comment("설명")
    @Column(length = 3000)
    private String description;
}
