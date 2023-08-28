package com.ysy.accountbook.domain.account.service;

import com.ysy.accountbook.domain.account.dto.*;
import com.ysy.accountbook.domain.account.dto.request.AccountRequest;
import com.ysy.accountbook.domain.account.dto.request.AccountSaveRequest;
import com.ysy.accountbook.domain.account.dto.response.AccountSaveResponse;
import com.ysy.accountbook.domain.account.entity.Account;
import com.ysy.accountbook.domain.account.entity.AccountType;
import com.ysy.accountbook.domain.account.entity.AssetType;
import com.ysy.accountbook.domain.account.exception.AccountNotDeleteException;
import com.ysy.accountbook.domain.account.exception.AccountNotFoundException;
import com.ysy.accountbook.domain.account.repository.AccountRepository;
import com.ysy.accountbook.domain.trade.domain_service.TradeSaveDomainService;
import com.ysy.accountbook.domain.user.entity.User;
import com.ysy.accountbook.domain.user.exception.UserNotFoundException;
import com.ysy.accountbook.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    final private AccountRepository accountRepository;
    final private AccountSaveService accountSaveService;
    final private UserRepository userRepository;
    final private TradeSaveDomainService tradeSaveDomainService;
    final private DefaultConversionService conversion;

    /**
     * 계정 리스트 (카테고리 리스트)
     *
     * @param email
     * @return
     */
    @Transactional
    public AccountsByTypeDto findAccountOrUser(String email) {
        User user = userRepository.findUserByEmailAndIsDelete(email, false)
                                  .orElseThrow();

        List<AccountDto> incomeAccounts = accountRepository.findAccountByUserAndAccountTypeAndIsDelete(user, AccountType.income, false)
                                                           .orElseThrow()
                                                           .stream()
                                                           .map(AccountDto::new)
                                                           .collect(Collectors.toList());

        List<AccountDto> expenseAccounts = accountRepository.findAccountByUserAndAccountTypeAndIsDelete(user, AccountType.expense, false)
                                                            .orElseThrow()
                                                            .stream()
                                                            .map(AccountDto::new)
                                                            .collect(Collectors.toList());

        List<AccountDto> assetAccounts = accountRepository.findAccountByUserAndAccountTypeAndIsDelete(user, AccountType.asset, false)
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
     * 자산 계정별 보유 금액 리스트
     *
     * @param email
     * @return
     */
    public List<AssetDto> findAssetAmountList(String email) {
        User user = userRepository.findUserByEmailAndIsDelete(email, false)
                                  .orElseThrow(UserNotFoundException::new);

        return accountRepository.findAssetAmountList(user.getUserId());
    }

    /**
     * 자산 계정 등록 및 변경
     *
     * @param asr
     * @return
     */
    @Transactional
    public AccountSaveResponse saveAssetAccount(AccountSaveRequest asr,
                                                String email) {
        User user = userRepository.findUserByEmailAndIsDelete(email, false)
                                  .orElseThrow(UserNotFoundException::new);

        Account account;

        if (asr.getAccountId() == null) {   // 신규 등록
            account = accountSaveService.saveNewAssetAccount(asr, user);

        } else {    // 변경
            account = accountRepository.findAccountByUserAndAccountId(user, asr.getAccountId())
                                       .orElseThrow(AccountNotFoundException::new);
            account.changeAccount(asr.getAccountName());
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
     * 계정 저장
     *
     * @param asr
     * @param user
     * @return
     */
    @Transactional
    public AccountSaveResponse saveAccount(AccountSaveRequest asr,
                                           User user) {


        Account account;

        if (asr.getAccountId() == null) {   // 신규 등록
            account = accountSaveService.saveNewAccount(asr, user);

        } else {    // 변경
            account = accountRepository.findAccountByUserAndAccountId(user, asr.getAccountId())
                                       .orElseThrow(AccountNotFoundException::new);
            account.changeAccount(asr.getAccountName());
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
     * 계정 삭제
     *
     * @param accountId 계정 ID
     * @param user      사용자
     * @return
     */
    @Transactional
    public AccountDto deleteAccount(Long accountId,
                                    User user) {

        Account account = accountRepository.findAccountByUserAndAccountId(user, accountId)
                                           .orElseThrow(AccountNotFoundException::new);
        if (account.isCanBeDeleted()) {
            account.delete();
        } else {
            throw new AccountNotDeleteException();
        }

        return new AccountDto(account);
    }

    /**
     * 계정 리스트 (카테고리 리스트)
     *
     * @param accountRequest
     * @return
     */
    @Transactional
    @Deprecated
    public List<AccountDto> findAccountsByUserId(AccountRequest accountRequest) {
        User user = userRepository.findById(accountRequest.getUserId())
                                  .orElseThrow();

        return accountRepository.findAccountByUser(user)
                                .orElseThrow()
                                .stream()
                                .map(AccountDto::new)
                                .collect(Collectors.toList());
    }
}
