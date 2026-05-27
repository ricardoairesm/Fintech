package com.fiap.fintech.service;

import com.fiap.fintech.dto.RequestDtos.RewardRequest;
import com.fiap.fintech.dto.ResponseDtos.RewardResponse;
import com.fiap.fintech.entity.Reward;
import com.fiap.fintech.exception.InvalidRequestException;
import com.fiap.fintech.exception.NotFoundException;
import com.fiap.fintech.repository.RewardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class RewardService {

    private final RewardRepository repository;

    public RewardService(RewardRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<RewardResponse> findAll() {
        return repository.findAll().stream().map(RewardResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public RewardResponse findById(int id) {
        return RewardResponse.from(getEntity(id));
    }

    private Reward getEntity(int id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Recompensa não encontrada."));
    }

    @Transactional
    public RewardResponse create(RewardRequest request) {
        validate(request);
        LocalDate now = LocalDate.now();
        Reward reward = new Reward();
        apply(reward, request);
        reward.setCreatedAt(now);
        reward.setUpdatedAt(now);
        return RewardResponse.from(repository.save(reward));
    }

    @Transactional
    public RewardResponse update(int id, RewardRequest request) {
        validate(request);
        Reward reward = getEntity(id);
        apply(reward, request);
        reward.setUpdatedAt(LocalDate.now());
        return RewardResponse.from(repository.save(reward));
    }

    @Transactional
    public void delete(int id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Recompensa não encontrada.");
        }
        repository.deleteById(id);
    }

    private static void apply(Reward reward, RewardRequest request) {
        reward.setName(request.name());
        reward.setDescription(request.description());
        reward.setActive(request.active() != null && request.active());
    }

    private static void validate(RewardRequest request) {
        if (request == null || request.name() == null || request.name().isBlank()) {
            throw new InvalidRequestException("Nome é obrigatório.");
        }
    }
}
