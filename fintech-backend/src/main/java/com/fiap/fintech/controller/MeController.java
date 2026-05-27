package com.fiap.fintech.controller;

import com.fiap.fintech.dto.RequestDtos.OwnBankAccountRequest;
import com.fiap.fintech.dto.RequestDtos.OwnGoalRequest;
import com.fiap.fintech.dto.RequestDtos.OwnTransactionRequest;
import com.fiap.fintech.dto.ResponseDtos.BankAccountResponse;
import com.fiap.fintech.dto.ResponseDtos.GoalResponse;
import com.fiap.fintech.dto.ResponseDtos.TransactionResponse;
import com.fiap.fintech.service.AuthService;
import com.fiap.fintech.service.BankAccountService;
import com.fiap.fintech.service.GoalService;
import com.fiap.fintech.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/me")
public class MeController {

    private final AuthService authService;
    private final BankAccountService bankAccountService;
    private final GoalService goalService;
    private final TransactionService transactionService;

    public MeController(AuthService authService, BankAccountService bankAccountService, GoalService goalService,
            TransactionService transactionService) {
        this.authService = authService;
        this.bankAccountService = bankAccountService;
        this.goalService = goalService;
        this.transactionService = transactionService;
    }

    @PostMapping("/accounts")
    public ResponseEntity<BankAccountResponse> createAccount(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody OwnBankAccountRequest request) {
        int userId = authService.requireAuthenticatedUserId(authorization);
        return ResponseEntity.status(HttpStatus.CREATED).body(bankAccountService.createOwn(userId, request));
    }

    @PostMapping("/goals")
    public ResponseEntity<GoalResponse> createGoal(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody OwnGoalRequest request) {
        int userId = authService.requireAuthenticatedUserId(authorization);
        return ResponseEntity.status(HttpStatus.CREATED).body(goalService.createOwn(userId, request));
    }

    @PostMapping("/transactions")
    public ResponseEntity<TransactionResponse> createTransaction(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody OwnTransactionRequest request) {
        int userId = authService.requireAuthenticatedUserId(authorization);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.createOwn(userId, request));
    }
}
