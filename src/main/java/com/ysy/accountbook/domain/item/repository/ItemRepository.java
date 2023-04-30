package com.ysy.accountbook.domain.item.repository;

import com.ysy.accountbook.domain.item.entity.Item;
import com.ysy.accountbook.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findItemByItemName(String itemName);
}
