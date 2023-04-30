package com.ysy.accountbook.domain.user.entity;

import com.ysy.accountbook.domain.base_entity.BaseEntity;
import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
@DynamicInsert
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

//    @OneToMany(mappedBy = "user")
//    private List<AccountCategory> accountCategory;
//    @OneToMany(mappedBy = "user_id")
//    private List<TradeDetail> incomeCategory;

    @Comment("이메일")
    @Column(length = 100)
    private String email;
    @Comment("이용가능여부")
    @Builder.Default
    @Column(nullable = false, columnDefinition = "tinyint(1) default 1")
    private Boolean isActivated = true;
}
