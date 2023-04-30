package com.ysy.accountbook.domain.trade.entity;

import com.ysy.accountbook.domain.base_entity.BaseEntity;
import com.ysy.accountbook.domain.trade_date.entity.TradeDate;
import com.ysy.accountbook.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@NoArgsConstructor
@ToString(callSuper = true)
public class Trade extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tradeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trade_date")
    private TradeDate tradeDate;

    public Trade(User user, TradeDate tradeDate) {
        this.user = user;
        this.tradeDate = tradeDate;
    }
}
