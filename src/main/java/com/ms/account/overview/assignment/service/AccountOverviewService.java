package com.ms.account.overview.assignment.service;

import com.ms.account.overview.assignment.api.*;
import com.ms.account.overview.assignment.exception.AccountsNotFoundException;
import com.ms.account.overview.assignment.exception.TransactionsNotFoundException;
import com.ms.account.overview.assignment.repository.AccountRepository;
import com.ms.account.overview.assignment.repository.TransactionRepository;
import com.ms.account.overview.assignment.repository.entity.Account;
import com.ms.account.overview.assignment.repository.entity.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountOverviewService {
    public static final String NO_ACCOUNTS_FOR_THE_USER = "No accounts for the user %s ";
    public static final String NO_TRANSACTIONS_FOR_THE_USER_ID_AND_ACCOUNT_ID = "No Transactions for the user id %s and account id %s";
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Method to get the list of Accounts for given user
     * @param userId
     * @return
     */
    public AccountListResponse getAccountsByUserId(String userId) {

        List<Account> accountRepositoryList = accountRepository.findByUserUserId(Long.valueOf(userId));

        if (accountRepositoryList.isEmpty()) {
            log.info("message=\"getAccountsByUserId is empty {} \"", accountRepositoryList.size());
            throw new AccountsNotFoundException(String.format(NO_ACCOUNTS_FOR_THE_USER, userId));
        }
        AccountListResponse accounts = AccountListResponse.builder()
                .accounts(accountRepositoryList.stream()
                        .map(account -> populateAccountList(account))
                        .collect(Collectors.toList()))
                .totalNoOfAccounts(accountRepositoryList.size())
                .build();

        return accounts;
    }

    private static AccountDTO populateAccountList(Account account) {
        return AccountDTO.builder()
                .id(account.getAccountId().toString())
                .name(account.getName())
                .type(account.getType())
                .number(account.getNumber().toString())
                .amount(Amount.builder()
                        .currencyCode(account.getCurrency())
                        .value(account.getBalance().toString())
                        .build())
                .balanceDate(account.getBalanceDate().toString())
                .status(AccountStatus.getValue(account.getStatus()))
                .build();
    }

    /**
     * Get the transactions for the given user and account
     * @param userId
     * @param accountId
     * @return
     */
    public TransactionListResponse getTransactionsByUserIdAndAccountId(String userId, String accountId) {

        List<Transaction> transactionRepositoryList =
                transactionRepository.findByAccountUserUserIdAndAccountAccountId(Long.valueOf(userId), Long.valueOf(accountId));

        if (transactionRepositoryList.isEmpty()) {
            log.info("message=\"getTransactionsByUserIdAndAccountId is empty {} \"", transactionRepositoryList.size());
            throw new TransactionsNotFoundException(String.format(NO_TRANSACTIONS_FOR_THE_USER_ID_AND_ACCOUNT_ID, userId, accountId));
        }
        TransactionListResponse transactionListResponse = TransactionListResponse.builder()
                .transactions(transactionRepositoryList.stream()
                                .map(transaction -> populateTransactionList(transaction))
                                .collect(Collectors.toList()))
                .totalNoOfTransactions(transactionRepositoryList.size())
                .build();

        return transactionListResponse;
    }

    private static TransactionDTO populateTransactionList(Transaction transaction) {
        return TransactionDTO.builder()
                .id(transaction.getTransactionId().toString()).description(transaction.getDescription())
                .transactionDate(transaction.getTransactionDate().toString())
                .currency(transaction.getCurrency())
                .amount(transaction.getAmount().toString())
                .type(transaction.getType())
                .number(transaction.getAccount().getNumber().toString())
                .build();
    }
}
