package com.fiap.fintech.service;

import com.fiap.fintech.dto.RequestDtos.TierRequest;
import com.fiap.fintech.dto.ResponseDtos.TierResponse;
import com.fiap.fintech.entity.Tier;
import com.fiap.fintech.exception.InvalidRequestException;
import com.fiap.fintech.exception.NotFoundException;
import com.fiap.fintech.repository.TierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TierService {

    private final TierRepository repository;

    public TierService(TierRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<TierResponse> findAll() {
        return repository.findAll().stream().map(TierResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public TierResponse findById(int id) {
        return TierResponse.from(getEntity(id));
    }

    private Tier getEntity(int id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Nível não encontrado."));
    }

    @Transactional
    public TierResponse create(TierRequest request) {
        validate(request);
        LocalDate now = LocalDate.now();
        Tier tier = new Tier();
        apply(tier, request);
        tier.setCreatedAt(now);
        tier.setUpdatedAt(now);
        return TierResponse.from(repository.save(tier));
    }

    @Transactional
    public TierResponse update(int id, TierRequest request) {
        validate(request);
        Tier tier = getEntity(id);
        apply(tier, request);
        tier.setUpdatedAt(LocalDate.now());
        return TierResponse.from(repository.save(tier));
    }

    @Transactional
    public void delete(int id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Nível não encontrado.");
        }
        repository.deleteById(id);
    }

    private static void apply(Tier tier, TierRequest request) {
        tier.setName(request.name());
        tier.setMinPointsRequired(request.minPointsRequired() == null ? 0 : request.minPointsRequired());
        tier.setHierarchy(request.hierarchy() == null ? 0 : request.hierarchy());
    }

    private static void validate(TierRequest request) {
        if (request == null || request.name() == null || request.name().isBlank()) {
            throw new InvalidRequestException("Nome é obrigatório.");
        }
    }
}
