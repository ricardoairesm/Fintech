package com.fiap.fintech.service;

import com.fiap.fintech.dto.RequestDtos.GoalRequest;
import com.fiap.fintech.dto.RequestDtos.OwnGoalRequest;
import com.fiap.fintech.dto.ResponseDtos.GoalResponse;
import com.fiap.fintech.entity.Goal;
import com.fiap.fintech.exception.InvalidRequestException;
import com.fiap.fintech.exception.NotFoundException;
import com.fiap.fintech.repository.GoalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class GoalService {

    private final GoalRepository repository;

    public GoalService(GoalRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<GoalResponse> findAll() {
        return repository.findAll().stream().map(GoalResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<GoalResponse> findByUser(int userId) {
        return repository.findByUserId(userId).stream().map(GoalResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public GoalResponse findById(int id) {
        return GoalResponse.from(getEntity(id));
    }

    public Goal getEntity(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Meta não encontrada."));
    }

    @Transactional
    public GoalResponse create(GoalRequest request) {
        if (request == null || request.userId() == null || request.userId() <= 0) {
            throw new InvalidRequestException("Usuário é obrigatório.");
        }
        requireText(request.title(), "Título");
        LocalDate now = LocalDate.now();
        Goal goal = new Goal();
        goal.setUserId(request.userId());
        goal.setTitle(request.title());
        goal.setAmount(request.amount() == null ? 0 : request.amount());
        goal.setSavedAmount(request.savedAmount() == null ? 0 : request.savedAmount());
        goal.setLimitDate(request.limitDate());
        goal.setCreatedAt(now);
        goal.setUpdatedAt(now);
        return GoalResponse.from(repository.save(goal));
    }

    @Transactional
    public GoalResponse createOwn(int userId, OwnGoalRequest request) {
        if (request == null) {
            throw new InvalidRequestException("Requisição inválida.");
        }
        requireText(request.title(), "Título");
        if (request.amount() == null || request.amount() <= 0) {
            throw new InvalidRequestException("Valor alvo deve ser maior que zero.");
        }
        LocalDate now = LocalDate.now();
        Goal goal = new Goal();
        goal.setUserId(userId);
        goal.setTitle(request.title());
        goal.setAmount(request.amount());
        goal.setSavedAmount(0d);
        goal.setLimitDate(request.limitDate());
        goal.setCreatedAt(now);
        goal.setUpdatedAt(now);
        return GoalResponse.from(repository.save(goal));
    }

    @Transactional
    public GoalResponse update(int id, GoalRequest request) {
        if (request == null || request.userId() == null || request.userId() <= 0) {
            throw new InvalidRequestException("Usuário é obrigatório.");
        }
        requireText(request.title(), "Título");
        Goal goal = getEntity(id);
        goal.setUserId(request.userId());
        goal.setTitle(request.title());
        goal.setAmount(request.amount() == null ? 0 : request.amount());
        goal.setSavedAmount(request.savedAmount() == null ? 0 : request.savedAmount());
        goal.setLimitDate(request.limitDate());
        goal.setUpdatedAt(LocalDate.now());
        return GoalResponse.from(repository.save(goal));
    }

    @Transactional
    public void delete(int id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Meta não encontrada.");
        }
        repository.deleteById(id);
    }

    @Transactional
    public void addToSavedAmount(Goal goal, double amount) {
        goal.setSavedAmount((goal.getSavedAmount() == null ? 0 : goal.getSavedAmount()) + amount);
        goal.setUpdatedAt(LocalDate.now());
        repository.save(goal);
    }

    private static void requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new InvalidRequestException(field + " é obrigatório.");
        }
    }
}
