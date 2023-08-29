package com.ysy.accountbook.domain.account.repository;

import com.ysy.accountbook.domain.account.dto.AssetDto;
import com.ysy.accountbook.domain.account.entity.Account;
import com.ysy.accountbook.domain.account.entity.AccountType;
import com.ysy.accountbook.domain.user.entity.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("DeprecatedIsStillUsed")
public interface AccountRepository extends JpaRepository<Account, Long>, AccountCustomRepository {
    Optional<Account> findAccountByUserAndAccountName(User user,
                                                      String accountName);

    Optional<Account> findAccountByUserAndAccountId(User user,
                                                    Long accountId);

    Optional<List<Account>> findAccountByUserAndAccountTypeAndIsDelete(User user,
                                                                       @NonNull AccountType accountType,
                                                                       boolean isDelete);

    @Query(nativeQuery = true, name = "account.findAssetAmountList")
    List<AssetDto> findAssetAmountList(@Param("userId") Long userId);

    @Procedure(procedureName = "init_account")
    void initAccount(Long userId);

    @Deprecated
    Optional<List<Account>> findAccountByUser(User user);
}
