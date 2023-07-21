package com.ysy.accountbook.domain.user.entity;

import com.ysy.accountbook.domain.base.BaseEntity;
import javax.persistence.*;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
@DynamicInsert
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
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

    @Comment("권한")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Comment("JWT 리프레쉬 토큰")
    @Column(length = 500)
    private String refreshToken;

    @Comment("이용가능여부")
    @Builder.Default
    @Column(nullable = false, columnDefinition = "tinyint(1) default 1")
    private Boolean isActivated = true;

    public User update(String name, String picture) {
        //this.name = name;
        //this.picture = picture;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
