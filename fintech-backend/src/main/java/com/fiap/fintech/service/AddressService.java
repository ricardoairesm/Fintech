package com.fiap.fintech.service;

import com.fiap.fintech.dto.RequestDtos.AddressRequest;
import com.fiap.fintech.dto.ResponseDtos.AddressResponse;
import com.fiap.fintech.entity.Address;
import com.fiap.fintech.exception.InvalidRequestException;
import com.fiap.fintech.exception.NotFoundException;
import com.fiap.fintech.repository.AddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AddressService {

    private final AddressRepository repository;

    public AddressService(AddressRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<AddressResponse> findAll() {
        return repository.findAll().stream().map(AddressResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public AddressResponse findById(int id) {
        return AddressResponse.from(getEntity(id));
    }

    @Transactional(readOnly = true)
    public AddressResponse findMainForUser(Integer mainAddressId) {
        if (mainAddressId == null) {
            return null;
        }
        return repository.findById(mainAddressId).map(AddressResponse::from).orElse(null);
    }

    private Address getEntity(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Endereço não encontrado."));
    }

    @Transactional
    public AddressResponse create(AddressRequest request) {
        validate(request);
        LocalDate now = LocalDate.now();
        Address address = new Address();
        apply(address, request);
        address.setCreatedAt(now);
        address.setUpdatedAt(now);
        return AddressResponse.from(repository.save(address));
    }

    @Transactional
    public AddressResponse update(int id, AddressRequest request) {
        validate(request);
        Address address = getEntity(id);
        apply(address, request);
        address.setUpdatedAt(LocalDate.now());
        return AddressResponse.from(repository.save(address));
    }

    @Transactional
    public void delete(int id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Endereço não encontrado.");
        }
        repository.deleteById(id);
    }

    private static void apply(Address address, AddressRequest request) {
        address.setUserId(request.userId());
        address.setAddressString(request.addressString());
        address.setZipCode(request.zipCode());
    }

    private static void validate(AddressRequest request) {
        if (request == null) {
            throw new InvalidRequestException("Requisição inválida.");
        }
        if (request.userId() == null || request.userId() <= 0) {
            throw new InvalidRequestException("Usuário é obrigatório.");
        }
        requireText(request.addressString(), "Endereço");
        requireText(request.zipCode(), "CEP");
    }

    private static void requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new InvalidRequestException(field + " é obrigatório.");
        }
    }
}
