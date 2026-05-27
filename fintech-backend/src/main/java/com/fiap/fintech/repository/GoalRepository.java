package com.fiap.fintech.repository;

import com.fiap.fintech.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Integer> {

    List<Goal> findByUserId(Integer userId);
}
