package com.fiap.fintech.repository;

import com.fiap.fintech.entity.CompletedChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompletedChallengeRepository extends JpaRepository<CompletedChallenge, Integer> {

    List<CompletedChallenge> findByUserId(Integer userId);
}
