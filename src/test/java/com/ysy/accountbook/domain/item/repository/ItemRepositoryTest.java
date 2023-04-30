package com.ysy.accountbook.domain.item.repository;

import com.ysy.accountbook.domain.account.entity.Account;
import com.ysy.accountbook.domain.account.repository.AccountRepository;
import com.ysy.accountbook.domain.item.entity.Item;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("local")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Test
    void saveItem() {
        Account account = accountRepository.findAccountByAccountName("현금성 자산").orElseThrow();

        Item item = new Item(account,"현금");
        itemRepository.save(item);
    }
}