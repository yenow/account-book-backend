package com.ysy.accountbook.domain.account.service;

import com.ysy.accountbook.domain.account.dto.*;
import com.ysy.accountbook.domain.account.entity.Account;
import com.ysy.accountbook.domain.account.entity.AccountType;
import com.ysy.accountbook.domain.account.entity.AssetType;
import com.ysy.accountbook.domain.account.exception.AccountNotFoundException;
import com.ysy.accountbook.domain.account.repository.AccountRepository;
import com.ysy.accountbook.domain.trade.service.TradeSaveDomainService;
import com.ysy.accountbook.domain.user.entity.User;
import com.ysy.accountbook.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    final private AccountRepository accountRepository;
    final private UserRepository userRepository;
    final private TradeSaveDomainService tradeSaveDomainService;
    final private DefaultConversionService conversion;

    /**
     * 계정 리스트 (카테고리 리스트)
     *
     * @param accountRequest
     * @return
     */
    @Transactional
    public List<AccountDto> findAccountsByUserId(AccountRequest accountRequest) {
        User user = userRepository.findById(accountRequest.getUserId())
                                  .orElseThrow();

        return accountRepository.findAccountByUser(user)
                                .orElseThrow()
                                .stream()
                                .map(AccountDto::new)
                                .collect(Collectors.toList());
    }

    /**
     * 계정 리스트 (카테고리 리스트)
     *
     * @param accountRequest
     * @return
     */
    @Transactional
    public AccountsByTypeDto findAccountByUserAndAccountType(AccountRequest accountRequest) {
        User user = userRepository.findById(accountRequest.getUserId())
                                  .orElseThrow();

        List<AccountDto> incomeAccounts = accountRepository.findAccountByUserAndAccountType(user, AccountType.income)
                                                           .orElseThrow()
                                                           .stream()
                                                           .map(AccountDto::new)
                                                           .collect(Collectors.toList());

        List<AccountDto> expenseAccounts = accountRepository.findAccountByUserAndAccountType(user, AccountType.expense)
                                                            .orElseThrow()
                                                            .stream()
                                                            .map(AccountDto::new)
                                                            .collect(Collectors.toList());

        List<AccountDto> assetAccounts = accountRepository.findAccountByUserAndAccountType(user, AccountType.asset)
                                                          .orElseThrow()
                                                          .stream()
                                                          .map(AccountDto::new)
                                                          .collect(Collectors.toList());

        return AccountsByTypeDto.builder()
                                .incomeAccounts(incomeAccounts)
                                .expenseAccounts(expenseAccounts)
                                .assetAccounts(assetAccounts)
                                .build();
    }

    /**
     * 자산 계정 저장
     *
     * @param asr
     * @return
     */
    @Transactional
    public AccountSaveResponse saveAssetAccount(AccountSaveRequest asr) {

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

                saveIncome(asr, account);
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


        return AccountSaveResponse.builder()
                                  .accountId(account.getAccountId())
                                  .accountName(account.getAccountName())
                                  .parentAccountId(asr.getAssetType() == AssetType.asset ? account.getParentAccount()
                                                                                                  .getAccountId() : null)
                                  .parentAccountName(asr.getAssetType() == AssetType.asset ? account.getParentAccount()
                                                                                                    .getAccountName() : "")
                                  .amount(asr.getAssetType() == AssetType.asset ? asr.getAmount() : 0L)
                                  .assetType(asr.getAssetType())
                                  .level(account.getLevel())
                                  .build();
    }

    /**
     * 첫 자산 등록시 금액은 '기타수입'으로 등록
     *
     * @param asr
     * @param account
     */
    private void saveIncome(AccountSaveRequest asr, Account account) {
        Account incomeAccount = accountRepository.findAccountByAccountName("기타")
                                                 .orElseThrow(AccountNotFoundException::new);

        tradeSaveDomainService.saveIncome(asr.getUserId(), conversion.convert(LocalDate.now(), String.class), asr.getAmount(),
                incomeAccount.getAccountId(), account.getAccountId(), "", ""
                                         );
    }
}
