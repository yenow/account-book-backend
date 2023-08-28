package com.ysy.accountbook.domain.trade.entity;

import com.ysy.accountbook.domain.base.BaseEntity;
import com.ysy.accountbook.domain.trade.dto.TradeSaveRequest;
import com.ysy.accountbook.domain.user.entity.User;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Trade extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tradeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NonNull
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trade_date")
    @NonNull
    private TradeDate tradeDate;
    @Builder.Default
    @OneToMany(mappedBy = "trade", fetch = FetchType.LAZY)
    private List<TradeDetail> tradeDetails = new ArrayList<>();

    @Comment("거래유형")
    @Column(length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    @NonNull
    private TradeType tradeType;
    @Comment("내용")
    @Column(length = 200)
    @NonNull
    private String content;
    @Comment("메모")
    @Column(length = 3000)
    @NonNull
    private String memo;

    public void updateTrade(String tradeDate,
                            TradeType tradeType,
                            String content,
                            String memo) {
        this.tradeDate = TradeDate.builder()
                                  .tradeDate(tradeDate)
                                  .build();
        this.tradeType = tradeType;
        this.content = content;
        this.memo = memo;
    }

    /**
     * 거래 삭제 (가계부 삭제)
     *
     */
    public void deleteTrade() {
        delete();
        tradeDetails.forEach(BaseEntity::delete);
    }
}
