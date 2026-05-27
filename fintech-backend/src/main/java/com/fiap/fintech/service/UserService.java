package com.fiap.fintech.service;

import com.fiap.fintech.dto.RequestDtos.UserRequest;
import com.fiap.fintech.dto.ResponseDtos.UserResponse;
import com.fiap.fintech.entity.User;
import com.fiap.fintech.exception.InvalidRequestException;
import com.fiap.fintech.exception.NotFoundException;
import com.fiap.fintech.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return repository.findAll().stream().map(UserResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public UserResponse findById(int id) {
        return UserResponse.from(getEntity(id));
    }

    public User getEntity(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
    }

    @Transactional
    public UserResponse create(UserRequest request) {
        validate(request);
        if (repository.existsByEmailIgnoreCase(request.email())) {
            throw new InvalidRequestException("Este e-mail já está cadastrado.");
        }
        LocalDate now = LocalDate.now();
        User user = new User();
        apply(user, request);
        user.setUserType(normalizeType(request.userType()));
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        return UserResponse.from(repository.save(user));
    }

    @Transactional
    public UserResponse update(int id, UserRequest request) {
        validate(request);
        User user = getEntity(id);
        apply(user, request);
        user.setUserType(normalizeType(request.userType()));
        user.setUpdatedAt(LocalDate.now());
        return UserResponse.from(repository.save(user));
    }

    @Transactional
    public void delete(int id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Usuário não encontrado.");
        }
        repository.deleteById(id);
    }

    private static void apply(User user, UserRequest request) {
        user.setUsername(request.username());
        user.setPassword(request.password());
        user.setEmail(request.email());
        user.setCelphone(request.celphone());
        user.setTierId(request.tierId());
        user.setPoints(request.points() == null ? 0 : request.points());
        user.setMainAddressId(request.mainAddressId());
        user.setMonthlyIncome(request.monthlyIncome() == null ? 0 : request.monthlyIncome());
        user.setMonthlySpending(request.monthlySpending() == null ? 0 : request.monthlySpending());
    }

    private static void validate(UserRequest request) {
        if (request == null) {
            throw new InvalidRequestException("Requisição inválida.");
        }
        requireText(request.username(), "Nome do usuário");
        requireText(request.password(), "Senha");
        requireText(request.email(), "E-mail");
        requireText(request.celphone(), "Telefone");
        if (request.tierId() == null || request.tierId() <= 0) {
            throw new InvalidRequestException("Nível é obrigatório.");
        }
    }

    private static String normalizeType(String userType) {
        if (userType == null) {
            return "USER";
        }
        String value = userType.trim().toUpperCase();
        if (!value.equals("USER") && !value.equals("ADMIN")) {
            throw new InvalidRequestException("Tipo de usuário deve ser USER ou ADMIN.");
        }
        return value;
    }

    private static void requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new InvalidRequestException(field + " é obrigatório.");
        }
    }
}
