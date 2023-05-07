package com.ysy.accountbook.domain.trade_bank_acount.repository;

import com.ysy.accountbook.domain.trade_bank_acount.entity.TradeBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeBankRepository extends JpaRepository<TradeBankAccount, Long> {

}
