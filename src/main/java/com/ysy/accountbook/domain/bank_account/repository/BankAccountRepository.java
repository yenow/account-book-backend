package com.ysy.accountbook.domain.bank_account.repository;

import com.ysy.accountbook.domain.bank_account.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

}
