package com.ysy.accountbook.domain.account.repository;

import com.ysy.accountbook.domain.account.dto.AssetDto;
import com.ysy.accountbook.domain.account.entity.Account;
import com.ysy.accountbook.domain.account.entity.AccountType;
import com.ysy.accountbook.domain.user.entity.User;
import com.ysy.accountbook.domain.user.repository.UserRepository;
import com.ysy.accountbook.global.common.util.Utility;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@SpringBootTest
@ActiveProfiles("local")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 계정 조회 테스트
     */
    @Test
    @Transactional
    void findAccountName() {
        User user = userRepository.findById(1L)
                                  .orElseThrow();
        Account findAccount = accountRepository.findAccountByUserAndAccountName(user, "현금")
                                               .orElseThrow();
        System.out.println("findAccount = " + findAccount);
    }

    @Test
    @Transactional
    void findAccountByUserAndAccountId() {
        User user = User.builder()
                        .userId(1L)
                        .build();

        Account account = accountRepository.findAccountByUserAndAccountId(user, 364L)
                                           .orElseThrow();
        System.out.println("account = " + account);
    }

    /**
     * 사용자의 모든 계정 조회
     */
    @Test
    @Transactional
    void findAccountByUser() {
        User user = User.builder()
                        .userId(3L)
                        .build();

        List<Account> accounts = accountRepository.findAccountByUser(user)
                                                  .orElseThrow();
        System.out.println("accounts = " + accounts);

    }

    /**
     * 자산 목록 조회
     */
    @Test
    @Transactional
    void findAssetList() {
        List<AssetDto> assetList = accountRepository.findAssetAmountList(1L);
        log.info("assetList :\n {}", Utility.prettyToString(assetList));
    }

    /**
     * 계정 저장 테스트
     */
    //@Test
    @Transactional
    void saveAccount() {
        User user = userRepository.findUserByEmail("phantom_ysy@naver.com")
                                  .orElseThrow();

        Account account = Account.builder()
                                 .accountType(AccountType.asset)
                                 .accountName("현금성 자산")
                                 .creationDate(LocalDateTime.now())
                                 .build();
        Account save = accountRepository.save(account);

        Account findAccount = accountRepository.findAccountByUserAndAccountName(user, account.getAccountName())
                                               .orElseThrow();
        log.info("findAccount : {}", findAccount);
    }
}