package com.fiap.fintech.controller;

import com.fiap.fintech.dto.RequestDtos.TierRequest;
import com.fiap.fintech.dto.ResponseDtos.TierResponse;
import com.fiap.fintech.service.TierService;
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
@RequestMapping("/api/tiers")
public class TierController {

    private final TierService service;

    public TierController(TierService service) {
        this.service = service;
    }

    @GetMapping
    public List<TierResponse> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public TierResponse getById(@PathVariable int id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<TierResponse> create(@RequestBody TierRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public TierResponse update(@PathVariable int id, @RequestBody TierRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
