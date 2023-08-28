package com.ysy.accountbook.domain.base;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseEntity {
    @Comment("생성날짜")
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime creationDate;
    @Comment("수정날짜")
    @LastModifiedDate
    private LocalDateTime modificationDate;
    @Comment("삭제여부")
    @Column(nullable = false, columnDefinition = "TINYINT", length = 1)
    private Boolean isDelete;

    /**
     * 수정날짜 변경
     *
     * @param modificationDate
     */
    public void changeModificationDate(LocalDateTime modificationDate) {
        this.modificationDate = modificationDate;
    }
    /**
     * 삭제
     *
     */
    public void delete() {
        this.modificationDate = LocalDateTime.now();
        this.isDelete = true;
    }
}
