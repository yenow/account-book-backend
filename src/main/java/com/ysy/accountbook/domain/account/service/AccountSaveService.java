package com.ysy.accountbook.domain.account.service;

import com.ysy.accountbook.domain.account.dto.request.AccountSaveRequest;
import com.ysy.accountbook.domain.account.entity.Account;
import com.ysy.accountbook.domain.account.entity.AccountType;
import com.ysy.accountbook.domain.account.exception.AccountNotFoundException;
import com.ysy.accountbook.domain.account.repository.AccountRepository;
import com.ysy.accountbook.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountSaveService {

    final private AccountRepository accountRepository;

    @Transactional
    public Account saveNewAssetAccount(AccountSaveRequest asr,
                                       User user) {
        Account account;
        switch (asr.getAssetType()) {
            case asset:
                Account parentAccount = accountRepository.findById(asr.getParentAccountId())
                                                         .orElseThrow(AccountNotFoundException::new);

                account = accountRepository.save(Account.builder()
                                                        .accountName(asr.getAccountName())
                                                        .accountType(AccountType.asset)
                                                        .parentAccount(parentAccount)
                                                        .isLeaf(true)
                                                        .level(2)
                                                        .build());

                saveIncome(asr, account, user);
                break;
            case group:
                account = accountRepository.save(Account.builder()
                                                        .accountName(asr.getAccountName())
                                                        .accountType(AccountType.asset)
                                                        .isLeaf(true)
                                                        .level(1)
                                                        .build());
                break;
            default:
                throw new RuntimeException();
        }
        return account;
    }

    @Transactional
    public Account saveNewAccount(AccountSaveRequest asr,
                                  User user) {
        return accountRepository.save(Account.builder()
                                             .accountName(asr.getAccountName())
                                             .accountType(asr.getAccountType())
                                             .user(user)
                                             .isLeaf(true)
                                             .canBeDeleted(true)
                                             .level(1)
                                             .build());
    }

    /**
     * 첫 자산 등록시 금액은 '기타수입'으로 등록
     *
     * @param asr
     * @param account
     */
    @Deprecated
    private void saveIncome(AccountSaveRequest asr,
                            Account account,
                            User user) {

        Account incomeAccount = accountRepository.findAccountByUserAndAccountName(user, "기타")
                                                 .orElseThrow(AccountNotFoundException::new);

        //tradeSaveDomainService.saveExpense(null,
        //                                   user.getUserId(),
        //                                   conversion.convert(LocalDate.now(), String.class),
        //                                   TradeType.income,
        //                                   asr.getAmount(),
        //                                   incomeAccount.getAccountId(),
        //                                   account.getAccountId(),
        //                                   "",
        //                                   "");
    }
}
