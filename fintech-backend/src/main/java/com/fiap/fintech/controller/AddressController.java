package com.fiap.fintech.controller;

import com.fiap.fintech.dto.RequestDtos.AddressRequest;
import com.fiap.fintech.dto.ResponseDtos.AddressResponse;
import com.fiap.fintech.service.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService service;

    public AddressController(AddressService service) {
        this.service = service;
    }

    @GetMapping
    public List<AddressResponse> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public AddressResponse getById(@PathVariable int id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<AddressResponse> create(@RequestBody AddressRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public AddressResponse update(@PathVariable int id, @RequestBody AddressRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
