package com.fiap.fintech.service;

import com.fiap.fintech.dto.RequestDtos.CompletedChallengeRequest;
import com.fiap.fintech.dto.ResponseDtos.CompletedChallengeResponse;
import com.fiap.fintech.entity.CompletedChallenge;
import com.fiap.fintech.exception.InvalidRequestException;
import com.fiap.fintech.exception.NotFoundException;
import com.fiap.fintech.repository.CompletedChallengeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CompletedChallengeService {

    private final CompletedChallengeRepository repository;

    public CompletedChallengeService(CompletedChallengeRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<CompletedChallengeResponse> findAll() {
        return repository.findAll().stream().map(CompletedChallengeResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<CompletedChallengeResponse> findByUser(int userId) {
        return repository.findByUserId(userId).stream().map(CompletedChallengeResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public CompletedChallengeResponse findById(int id) {
        return CompletedChallengeResponse.from(getEntity(id));
    }

    private CompletedChallenge getEntity(int id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Desafio concluído não encontrado."));
    }

    @Transactional
    public CompletedChallengeResponse create(CompletedChallengeRequest request) {
        validate(request);
        LocalDate now = LocalDate.now();
        CompletedChallenge entity = new CompletedChallenge();
        apply(entity, request);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        return CompletedChallengeResponse.from(repository.save(entity));
    }

    @Transactional
    public CompletedChallengeResponse update(int id, CompletedChallengeRequest request) {
        validate(request);
        CompletedChallenge entity = getEntity(id);
        apply(entity, request);
        entity.setUpdatedAt(LocalDate.now());
        return CompletedChallengeResponse.from(repository.save(entity));
    }

    @Transactional
    public void delete(int id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Desafio concluído não encontrado.");
        }
        repository.deleteById(id);
    }

    private static void apply(CompletedChallenge entity, CompletedChallengeRequest request) {
        entity.setUserId(request.userId());
        entity.setChallengeId(request.challengeId());
        entity.setCompletedAt(request.completedAt());
    }

    private static void validate(CompletedChallengeRequest request) {
        if (request == null || request.userId() == null || request.userId() <= 0) {
            throw new InvalidRequestException("Usuário é obrigatório.");
        }
        if (request.challengeId() == null || request.challengeId() <= 0) {
            throw new InvalidRequestException("Desafio é obrigatório.");
        }
    }
}
