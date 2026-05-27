package com.fiap.fintech.controller;

import com.fiap.fintech.dto.RequestDtos.CompletedChallengeRequest;
import com.fiap.fintech.dto.ResponseDtos.CompletedChallengeResponse;
import com.fiap.fintech.service.CompletedChallengeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/completed-challenges")
public class CompletedChallengeController {

    private final CompletedChallengeService service;

    public CompletedChallengeController(CompletedChallengeService service) {
        this.service = service;
    }

    @GetMapping
    public List<CompletedChallengeResponse> list(@RequestParam(value = "userId", required = false) Integer userId) {
        return userId == null ? service.findAll() : service.findByUser(userId);
    }

    @GetMapping("/{id}")
    public CompletedChallengeResponse getById(@PathVariable int id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<CompletedChallengeResponse> create(@RequestBody CompletedChallengeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public CompletedChallengeResponse update(@PathVariable int id, @RequestBody CompletedChallengeRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
