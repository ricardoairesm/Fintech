package com.fiap.fintech.service;

import com.fiap.fintech.dto.RequestDtos.BankAccountRequest;
import com.fiap.fintech.dto.RequestDtos.OwnBankAccountRequest;
import com.fiap.fintech.dto.ResponseDtos.BankAccountResponse;
import com.fiap.fintech.entity.BankAccount;
import com.fiap.fintech.exception.InvalidRequestException;
import com.fiap.fintech.exception.NotFoundException;
import com.fiap.fintech.repository.BankAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BankAccountService {

    private final BankAccountRepository repository;

    public BankAccountService(BankAccountRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<BankAccountResponse> findAll() {
        return repository.findAll().stream().map(BankAccountResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<BankAccountResponse> findByUser(int userId) {
        return repository.findByUserId(userId).stream().map(BankAccountResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public BankAccountResponse findById(int id) {
        return BankAccountResponse.from(getEntity(id));
    }

    private BankAccount getEntity(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Conta bancária não encontrada."));
    }

    public boolean userOwnsAccount(int userId, int accountId) {
        return repository.findById(accountId).map(account -> account.getUserId() == userId).orElse(false);
    }

    @Transactional
    public BankAccountResponse create(BankAccountRequest request) {
        if (request == null || request.userId() == null || request.userId() <= 0) {
            throw new InvalidRequestException("Usuário é obrigatório.");
        }
        return BankAccountResponse.from(persist(new BankAccount(), request.userId(), request.bank(), request.type(),
                request.description(), request.agency(), request.accountNumber(), true));
    }

    @Transactional
    public BankAccountResponse createOwn(int userId, OwnBankAccountRequest request) {
        if (request == null) {
            throw new InvalidRequestException("Requisição inválida.");
        }
        return BankAccountResponse.from(persist(new BankAccount(), userId, request.bank(), request.type(),
                request.description(), request.agency(), request.accountNumber(), true));
    }

    @Transactional
    public BankAccountResponse update(int id, BankAccountRequest request) {
        if (request == null || request.userId() == null || request.userId() <= 0) {
            throw new InvalidRequestException("Usuário é obrigatório.");
        }
        return BankAccountResponse.from(persist(getEntity(id), request.userId(), request.bank(), request.type(),
                request.description(), request.agency(), request.accountNumber(), false));
    }

    @Transactional
    public void delete(int id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Conta bancária não encontrada.");
        }
        repository.deleteById(id);
    }

    private BankAccount persist(BankAccount account, int userId, String bank, String type, String description,
            String agency, String accountNumber, boolean isNew) {
        requireText(bank, "Banco");
        requireText(type, "Tipo");
        requireText(description, "Descrição");
        requireText(agency, "Agência");
        requireText(accountNumber, "Conta");
        LocalDate now = LocalDate.now();
        account.setUserId(userId);
        account.setBank(bank);
        account.setType(type);
        account.setDescription(description);
        account.setAgency(agency);
        account.setAccountNumber(accountNumber);
        if (isNew) {
            account.setCreatedAt(now);
        }
        account.setUpdatedAt(now);
        return repository.save(account);
    }

    private static void requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new InvalidRequestException(field + " é obrigatório.");
        }
    }
}
