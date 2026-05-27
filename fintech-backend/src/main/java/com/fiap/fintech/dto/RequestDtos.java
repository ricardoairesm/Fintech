package com.fiap.fintech.dto;

import java.time.LocalDate;

public final class RequestDtos {

    private RequestDtos() {
    }

    public record LoginRequest(String email, String password) {
    }

    public record RegisterRequest(
            String username,
            String password,
            String email,
            String celphone,
            Integer monthlyIncome,
            Integer monthlySpending) {
    }

    public record UserRequest(
            String username,
            String password,
            String email,
            String celphone,
            String userType,
            Integer tierId,
            Integer points,
            Integer mainAddressId,
            Integer monthlyIncome,
            Integer monthlySpending) {
    }

    public record AddressRequest(Integer userId, String addressString, String zipCode) {
    }

    public record BankAccountRequest(
            Integer userId,
            String bank,
            String type,
            String description,
            String agency,
            String accountNumber) {
    }

    public record OwnBankAccountRequest(
            String bank,
            String type,
            String description,
            String agency,
            String accountNumber) {
    }

    public record TransactionRequest(
            Integer userId,
            Double amount,
            String type,
            String description,
            LocalDate transactionDate,
            Integer bankAccountId,
            Integer yield,
            Integer goalId) {
    }

    public record OwnTransactionRequest(
            Double amount,
            String type,
            String description,
            LocalDate transactionDate,
            Integer bankAccountId,
            Integer yield,
            Integer goalId) {
    }

    public record GoalRequest(
            Integer userId,
            String title,
            Double amount,
            Double savedAmount,
            LocalDate limitDate) {
    }

    public record OwnGoalRequest(String title, Double amount, LocalDate limitDate) {
    }

    public record TierRequest(String name, Integer minPointsRequired, Integer hierarchy) {
    }

    public record RewardRequest(String name, String description, Boolean active) {
    }

    public record ChallengeRequest(
            String title,
            Integer minTierId,
            LocalDate startDate,
            LocalDate endDate,
            Boolean active,
            Integer rewardId,
            Integer progress) {
    }

    public record CompletedChallengeRequest(Integer userId, Integer challengeId, LocalDate completedAt) {
    }
}
