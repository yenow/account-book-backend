package com.ysy.accountbook.domain.commoncode.entity;

import com.ysy.accountbook.global.common.domain.BaseEntity;
import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@NoArgsConstructor
@ToString(callSuper = true, exclude = {"childrenCommonCode"})
public class CommonCode extends BaseEntity {
    @Id
    @Column(length = 200)
    private String codeId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_code_id")
    private CommonCode parentCommonCode;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentCommonCode")
    private List<CommonCode> childrenCommonCode;

    @Comment("코드명")
    @Column(length = 200, nullable = false)
    private String codeName;

    @Comment("사용여부")
    @Column(nullable = false, columnDefinition = "tinyint(1) default 1")
    private boolean isUse = true;

    @Comment("정렬 순번")
    @Column(length = 3)
    private Integer orderNumber = 0;

    @Comment("설명")
    @Column(length = 1000)
    private String description;

    @Builder
    public CommonCode(String codeId, CommonCode parentCommonCode, List<CommonCode> childrenCommonCode, String codeName, boolean isUse, Integer orderNumber, String description, LocalDateTime createdDate, LocalDateTime updatedDate, LocalDateTime deletedDate, Long createdUserId, Long updatedUserId) {
        super(createdDate,updatedDate,deletedDate,createdUserId,updatedUserId);
        this.codeId = codeId;
        this.parentCommonCode = parentCommonCode;
        this.childrenCommonCode = childrenCommonCode;
        this.codeName = codeName;
        this.isUse = isUse;
        this.orderNumber = orderNumber;
        this.description = description;
    }
}
