package com.ysy.accountbook.domain.user.entity;

import com.ysy.accountbook.domain.base.BaseEntity;
import javax.persistence.*;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.UniqueElements;

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

    @Comment("이메일")
    @Column(length = 100, unique = true)
    private String email;
    @Comment("이름")
    @Column(length = 100)
    private String name;

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

    @Transient
    private final String password = Password.PASSWORD;

    public User update(String name, String picture) {
        this.name = name;
        //this.picture = picture;
        return this;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void logout() {
        this.refreshToken = "";
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
