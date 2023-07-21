package com.ysy.accountbook.domain.common_code.entity;

import com.ysy.accountbook.domain.base.BaseEntity;
import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@NoArgsConstructor
@ToString(callSuper = true, exclude = {"childrenCommonCode"})
public class CommonCode extends BaseEntity {
    @Id
    @Column(length = 100)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_code")
    private CommonCode parentCommonCode;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentCommonCode")
    private List<CommonCode> childrenCommonCode;

    @Comment("코드명")
    @Column(length = 200, nullable = false)
    private String codeName;

    @Comment("사용여부")
    @Column(nullable = false, columnDefinition = "TINYINT(1) default 1", length = 1)
    private boolean isUse;

    @Comment("정렬 순번")
    @Column(length = 3)
    private Integer orderNumber = 0;

    @Comment("설명")
    @Column(length = 1000)
    private String description;
}
