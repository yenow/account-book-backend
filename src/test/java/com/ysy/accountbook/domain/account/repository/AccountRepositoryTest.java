package com.ysy.accountbook.domain.account.repository;

import com.ysy.accountbook.domain.account.entity.Account;
import com.ysy.accountbook.domain.account.entity.AccountType;
import com.ysy.accountbook.domain.user.entity.User;
import com.ysy.accountbook.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("local")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void account() {
        Account account = Account.builder()
                .accountType(AccountType.asset)
                .accountName("현금성 자산")
                .creationDate(LocalDateTime.now())
                .build();
        log.info("account:{}",account);
    }

    @Test
    void saveAccount() {
        User user = userRepository.findUserByEmail("test@google.com").orElseThrow();

//        Account account = new Account(user,null, AccountType.asset,"현금성 자산");
        Account account = Account.builder()
                .accountType(AccountType.asset)
                .accountName("현금성 자산")
                .creationDate(LocalDateTime.now())
                .build();
        Account save = accountRepository.save(account);

        Account findAccount = accountRepository.findAccountByAccountName(account.getAccountName()).orElseThrow();
//        log.info("findAccount : {}",findAccount);
    }

    @Test
    @Transactional
    void findAccountName() {
        Account findAccount = accountRepository.findAccountByAccountName("테스트계정").orElseThrow();
        System.out.println("findAccount = " + findAccount);
    }
}