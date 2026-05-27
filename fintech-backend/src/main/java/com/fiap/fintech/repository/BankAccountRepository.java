package com.fiap.fintech.repository;

import com.fiap.fintech.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {

    List<BankAccount> findByUserId(Integer userId);
}
