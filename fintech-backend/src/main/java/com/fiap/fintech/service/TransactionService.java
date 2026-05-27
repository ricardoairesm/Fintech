package com.fiap.fintech.service;

import com.fiap.fintech.dto.RequestDtos.OwnTransactionRequest;
import com.fiap.fintech.dto.RequestDtos.TransactionRequest;
import com.fiap.fintech.dto.ResponseDtos.DashboardSummaryResponse;
import com.fiap.fintech.dto.ResponseDtos.TransactionResponse;
import com.fiap.fintech.entity.Goal;
import com.fiap.fintech.entity.Transaction;
import com.fiap.fintech.exception.InvalidRequestException;
import com.fiap.fintech.exception.NotFoundException;
import com.fiap.fintech.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {

    private static final String INVESTIMENTO = "Investimento";

    private final TransactionRepository repository;
    private final BankAccountService bankAccountService;
    private final GoalService goalService;

    public TransactionService(TransactionRepository repository, BankAccountService bankAccountService,
            GoalService goalService) {
        this.repository = repository;
        this.bankAccountService = bankAccountService;
        this.goalService = goalService;
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> findAll() {
        return repository.findAll().stream().map(TransactionResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> findByUser(int userId) {
        return repository.findByUserIdOrderByTransactionDateDescIdDesc(userId).stream()
                .map(TransactionResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public TransactionResponse findById(int id) {
        return TransactionResponse.from(getEntity(id));
    }

    private Transaction getEntity(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transação não encontrada."));
    }

    @Transactional
    public TransactionResponse create(TransactionRequest request) {
        if (request == null || request.userId() == null || request.userId() <= 0) {
            throw new InvalidRequestException("Usuário é obrigatório.");
        }
        validateCommon(request.amount(), request.type(), request.description(), request.bankAccountId());
        Transaction transaction = new Transaction();
        LocalDate now = LocalDate.now();
        transaction.setUserId(request.userId());
        transaction.setAmount(request.amount());
        transaction.setType(request.type());
        transaction.setDescription(request.description());
        transaction.setTransactionDate(request.transactionDate());
        transaction.setBankAccountId(request.bankAccountId());
        transaction.setYield(request.yield());
        transaction.setGoalId(request.goalId());
        transaction.setCreatedAt(now);
        transaction.setUpdatedAt(now);
        return TransactionResponse.from(repository.save(transaction));
    }

    @Transactional
    public TransactionResponse createOwn(int userId, OwnTransactionRequest request) {
        if (request == null) {
            throw new InvalidRequestException("Requisição inválida.");
        }
        validateCommon(request.amount(), request.type(), request.description(), request.bankAccountId());
        if (!bankAccountService.userOwnsAccount(userId, request.bankAccountId())) {
            throw new InvalidRequestException("A conta bancária selecionada não pertence ao usuário autenticado.");
        }

        Goal goal = null;
        if (request.goalId() != null) {
            goal = goalService.getEntity(request.goalId());
            if (goal.getUserId() != userId) {
                throw new InvalidRequestException("A meta selecionada não pertence ao usuário autenticado.");
            }
        }

        Transaction transaction = new Transaction();
        LocalDate now = LocalDate.now();
        transaction.setUserId(userId);
        transaction.setAmount(request.amount());
        transaction.setType(request.type());
        transaction.setDescription(request.description());
        transaction.setTransactionDate(request.transactionDate());
        transaction.setBankAccountId(request.bankAccountId());
        transaction.setYield(request.yield());
        transaction.setGoalId(request.goalId());
        transaction.setCreatedAt(now);
        transaction.setUpdatedAt(now);
        Transaction saved = repository.save(transaction);

        if (goal != null && INVESTIMENTO.equalsIgnoreCase(request.type())) {
            goalService.addToSavedAmount(goal, request.amount());
        }
        return TransactionResponse.from(saved);
    }

    @Transactional
    public TransactionResponse update(int id, TransactionRequest request) {
        if (request == null || request.userId() == null || request.userId() <= 0) {
            throw new InvalidRequestException("Usuário é obrigatório.");
        }
        validateCommon(request.amount(), request.type(), request.description(), request.bankAccountId());
        Transaction transaction = getEntity(id);
        transaction.setUserId(request.userId());
        transaction.setAmount(request.amount());
        transaction.setType(request.type());
        transaction.setDescription(request.description());
        transaction.setTransactionDate(request.transactionDate());
        transaction.setBankAccountId(request.bankAccountId());
        transaction.setYield(request.yield());
        transaction.setGoalId(request.goalId());
        transaction.setUpdatedAt(LocalDate.now());
        return TransactionResponse.from(repository.save(transaction));
    }

    @Transactional
    public void delete(int id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Transação não encontrada.");
        }
        repository.deleteById(id);
    }

    public DashboardSummaryResponse summarize(List<TransactionResponse> transactions) {
        double income = sumByType(transactions, "Receita");
        double expense = sumByType(transactions, "Despesa");
        double invested = sumByType(transactions, INVESTIMENTO);
        return new DashboardSummaryResponse(income - expense - invested, income, expense, invested);
    }

    private static double sumByType(List<TransactionResponse> transactions, String type) {
        return transactions.stream()
                .filter(transaction -> type.equalsIgnoreCase(transaction.type()))
                .mapToDouble(transaction -> transaction.amount() == null ? 0 : transaction.amount())
                .sum();
    }

    private static void validateCommon(Double amount, String type, String description, Integer bankAccountId) {
        if (amount == null || amount <= 0) {
            throw new InvalidRequestException("Valor deve ser maior que zero.");
        }
        if (type == null || type.isBlank()) {
            throw new InvalidRequestException("Tipo é obrigatório.");
        }
        if (description == null || description.isBlank()) {
            throw new InvalidRequestException("Descrição é obrigatória.");
        }
        if (bankAccountId == null || bankAccountId <= 0) {
            throw new InvalidRequestException("Conta bancária é obrigatória.");
        }
    }
}
