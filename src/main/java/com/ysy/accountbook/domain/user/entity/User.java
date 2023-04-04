package com.ysy.accountbook.domain.user.entity;

import com.ysy.accountbook.global.common.domain.BaseEntity;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
@DynamicInsert
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@ToString(callSuper = true, exclude = {"post"})
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
//    @OneToMany(mappedBy = "user")
//    @Builder.Default
//    private List<Post> post = new ArrayList<>();

    @Comment("이메일")
    @Column(length = 100)
    private String email;
    @Comment("별명")
    @Column(length = 50, nullable = false)
    private String nickname;
    @Comment("생년월일")
    @Column(length = 8, nullable = false)
    private String birthDay;
    @Comment("성별")
    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Comment("비밀번호")
    @Column(length = 200)
    private String password;
    @Comment("이용가능여부")
    @Builder.Default
    @Column(nullable = false, columnDefinition = "tinyint(1) default 1")
    private Boolean isActivated = true;
}
