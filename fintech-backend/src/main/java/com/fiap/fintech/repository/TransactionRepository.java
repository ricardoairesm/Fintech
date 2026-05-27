package com.fiap.fintech.repository;

import com.fiap.fintech.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findByUserIdOrderByTransactionDateDescIdDesc(Integer userId);
}
