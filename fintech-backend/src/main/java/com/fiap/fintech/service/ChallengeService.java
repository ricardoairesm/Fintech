package com.fiap.fintech.service;

import com.fiap.fintech.dto.RequestDtos.ChallengeRequest;
import com.fiap.fintech.dto.ResponseDtos.ChallengeResponse;
import com.fiap.fintech.entity.Challenge;
import com.fiap.fintech.exception.InvalidRequestException;
import com.fiap.fintech.exception.NotFoundException;
import com.fiap.fintech.repository.ChallengeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ChallengeService {

    private final ChallengeRepository repository;

    public ChallengeService(ChallengeRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ChallengeResponse> findAll() {
        return repository.findAll().stream().map(ChallengeResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ChallengeResponse findById(int id) {
        return ChallengeResponse.from(getEntity(id));
    }

    private Challenge getEntity(int id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Desafio não encontrado."));
    }

    @Transactional
    public ChallengeResponse create(ChallengeRequest request) {
        validate(request);
        LocalDate now = LocalDate.now();
        Challenge challenge = new Challenge();
        apply(challenge, request);
        challenge.setCreatedAt(now);
        challenge.setUpdatedAt(now);
        return ChallengeResponse.from(repository.save(challenge));
    }

    @Transactional
    public ChallengeResponse update(int id, ChallengeRequest request) {
        validate(request);
        Challenge challenge = getEntity(id);
        apply(challenge, request);
        challenge.setUpdatedAt(LocalDate.now());
        return ChallengeResponse.from(repository.save(challenge));
    }

    @Transactional
    public void delete(int id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Desafio não encontrado.");
        }
        repository.deleteById(id);
    }

    private static void apply(Challenge challenge, ChallengeRequest request) {
        challenge.setTitle(request.title());
        challenge.setMinTierId(request.minTierId() == null ? 0 : request.minTierId());
        challenge.setStartDate(request.startDate());
        challenge.setEndDate(request.endDate());
        challenge.setActive(request.active() != null && request.active());
        challenge.setRewardId(request.rewardId() == null ? 0 : request.rewardId());
        challenge.setProgress(request.progress() == null ? 0 : request.progress());
    }

    private static void validate(ChallengeRequest request) {
        if (request == null || request.title() == null || request.title().isBlank()) {
            throw new InvalidRequestException("Título é obrigatório.");
        }
        if (request.progress() != null && (request.progress() < 0 || request.progress() > 100)) {
            throw new InvalidRequestException("Progresso deve estar entre 0 e 100.");
        }
    }
}
