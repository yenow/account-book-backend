package com.ysy.accountbook.domain.item.entity;

import com.ysy.accountbook.domain.account.entity.Account;
import com.ysy.accountbook.domain.account.entity.AccountType;
import com.ysy.accountbook.domain.base_entity.BaseEntity;
import com.ysy.accountbook.domain.trade_detail.entity.TradeDetail;
import com.ysy.accountbook.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@NoArgsConstructor
@ToString(callSuper = true)
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
    @OneToMany(mappedBy = "item")
    private List<TradeDetail> tradeDetailList;

    @Comment("항목명")
    @Column(length = 100, nullable = false)
    private String itemName;

    public Item(Account account, String itemName) {
        this.account = account;
        this.itemName = itemName;
    }
}
