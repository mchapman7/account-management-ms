package com.ms.account.management.repository;

import com.ms.account.management.repository.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountUserUserIdAndAccountAccountId(Long userId, Long accountId);
}
