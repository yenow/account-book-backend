package com.ysy.accountbook.global.common.domain;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {
    @Comment("생성날짜")
    @CreatedDate
    private LocalDateTime createdDate;
    @Comment("수정날짜")
    @LastModifiedDate
    private LocalDateTime updatedDate;
    @Comment("삭제날짜")
    @Column
    private LocalDateTime deletedDate;

    @Comment("생성자")
    @CreatedBy
    @Column(updatable = false)
    private Long createdUserId;
    @Comment("수정자")
    @LastModifiedBy
    private Long updatedUserId;
}
