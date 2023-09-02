package com.ysy.accountbook.domain.notify.dto;

import com.ysy.accountbook.domain.notify.entity.Notify;
import com.ysy.accountbook.global.common.util.Utility;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class NotifyDto {
    private Long notifyId;
    private String title;
    private String content;
    private String creationDate;
    private String modificationDate;

    public NotifyDto(Notify notify) {
        this.notifyId = notify.getNotifyId();
        this.title = notify.getTitle();
        this.content = notify.getContent();
        this.creationDate = Utility.dateToString(notify.getCreationDate());
        this.modificationDate = notify.getModificationDate() != null ? Utility.dateToString(notify.getModificationDate()) : this.creationDate;
    }
}
