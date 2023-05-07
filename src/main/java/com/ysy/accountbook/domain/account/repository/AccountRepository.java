package com.ysy.accountbook.domain.account.repository;

import com.ysy.accountbook.domain.account.entity.Account;
import com.ysy.accountbook.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findAccountByAccountName(String accountName);
    Optional<Account> findAccountByUserAndAccountId(User user, Long accountId);
}
