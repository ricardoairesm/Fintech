package com.fiap.fintech.api.dto;

import com.fiap.fintech.model.Address;
import com.fiap.fintech.model.BankAccount;
import com.fiap.fintech.model.Challenge;
import com.fiap.fintech.model.CompletedChallenge;
import com.fiap.fintech.model.Goal;
import com.fiap.fintech.model.Reward;
import com.fiap.fintech.model.Tier;
import com.fiap.fintech.model.Transaction;
import com.fiap.fintech.model.User;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public final class ApiDtos {

    private ApiDtos() {
    }

    public record UserResponse(
            int id,
            String username,
            String email,
            String celphone,
            String userType,
            int tierId,
            int points,
            Integer mainAddressId,
            int monthlyIncome,
            int monthlySpending) {

        public static UserResponse from(User user) {
            return new UserResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getCelphone(),
                    user.getUserType(),
                    user.getTierId(),
                    user.getPoints(),
                    user.getMainAddressId(),
                    user.getMonthlyIncome(),
                    user.getMonthlySpending());
        }
    }

    public record AddressResponse(int id, int userId, String addressString, String zipCode) {
        public static AddressResponse from(Address address) {
            return new AddressResponse(address.getId(), address.getUserId(), address.getAddressString(), address.getZipCode());
        }
    }

    public record BankAccountResponse(
            int id,
            int userId,
            String bank,
            String type,
            String description,
            String agency,
            String accountNumber) {

        public static BankAccountResponse from(BankAccount account) {
            return new BankAccountResponse(
                    account.getId(),
                    account.getUserId(),
                    account.getBank(),
                    account.getType(),
                    account.getDescription(),
                    account.getAgency(),
                    account.getAccountNumber());
        }
    }

    public record TransactionResponse(
            int id,
            int userId,
            double amount,
            String type,
            String description,
            String transactionDate,
            int bankAccountId,
            Integer yield,
            Integer goalId) {

        public static TransactionResponse from(Transaction transaction) {
            return new TransactionResponse(
                    transaction.getId(),
                    transaction.getUserId(),
                    transaction.getAmount(),
                    transaction.getType(),
                    transaction.getDescription(),
                    toIsoDate(transaction.getTransactionDate()),
                    transaction.getBankAccountId(),
                    transaction.getYield(),
                    transaction.getGoalId());
        }
    }

    public record GoalResponse(
            int id,
            int userId,
            String title,
            double amount,
            double savedAmount,
            String limitDate) {

        public static GoalResponse from(Goal goal) {
            return new GoalResponse(
                    goal.getId(),
                    goal.getUserId(),
                    goal.getTitle(),
                    goal.getAmount(),
                    goal.getSavedAmount(),
                    toIsoDate(goal.getLimitDate()));
        }
    }

    public record TierResponse(int id, String name, int minPointsRequired, int hierarchy) {
        public static TierResponse from(Tier tier) {
            return new TierResponse(tier.getId(), tier.getName(), tier.getMinPointsRequired(), tier.getHierarchy());
        }
    }

    public record RewardResponse(int id, String name, String description, boolean active) {
        public static RewardResponse from(Reward reward) {
            return new RewardResponse(reward.getId(), reward.getName(), reward.getDescription(), reward.isActive());
        }
    }

    public record ChallengeResponse(
            int id,
            String title,
            int minTierId,
            String startDate,
            String endDate,
            boolean active,
            int rewardId,
            int progress) {

        public static ChallengeResponse from(Challenge challenge) {
            return new ChallengeResponse(
                    challenge.getId(),
                    challenge.getTitle(),
                    challenge.getMinTierId(),
                    toIsoDate(challenge.getStartDate()),
                    toIsoDate(challenge.getEndDate()),
                    challenge.isActive(),
                    challenge.getRewardId(),
                    challenge.getProgress());
        }
    }

    public record CompletedChallengeResponse(int id, int userId, int challengeId, String completedAt) {
        public static CompletedChallengeResponse from(CompletedChallenge challenge) {
            return new CompletedChallengeResponse(
                    challenge.getId(),
                    challenge.getUserId(),
                    challenge.getChallengeId(),
                    toIsoDate(challenge.getCompletedAt()));
        }
    }

    public record DashboardResponse(
            UserResponse user,
            AddressResponse address,
            List<BankAccountResponse> accounts,
            List<TransactionResponse> transactions,
            List<GoalResponse> goals,
            List<TierResponse> tiers,
            List<RewardResponse> rewards,
            List<ChallengeResponse> challenges,
            List<CompletedChallengeResponse> completedChallenges,
            DashboardSummaryResponse summary) {
    }

    public record DashboardSummaryResponse(
            double balance,
            double totalIncome,
            double totalExpense,
            double totalInvested) {
    }

    public record AdminEntitiesResponse(
            List<UserResponse> users,
            List<AddressResponse> addresses,
            List<BankAccountResponse> accounts,
            List<TransactionResponse> transactions,
            List<GoalResponse> goals,
            List<TierResponse> tiers,
            List<RewardResponse> rewards,
            List<ChallengeResponse> challenges,
            List<CompletedChallengeResponse> completedChallenges) {
    }

    public record CreateUserRequest(
            String username,
            String password,
            String email,
            String celphone,
            String userType,
            int tierId,
            int points,
            Integer mainAddressId,
            int monthlyIncome,
            int monthlySpending) {
    }

    public record CreateAddressRequest(int userId, String addressString, String zipCode) {
    }

    public record CreateBankAccountRequest(
            int userId,
            String bank,
            String type,
            String description,
            String agency,
            String accountNumber) {
    }

    public record CreateOwnBankAccountRequest(
            String bank,
            String type,
            String description,
            String agency,
            String accountNumber) {
    }

    public record CreateTransactionRequest(
            int userId,
            double amount,
            String type,
            String description,
            String transactionDate,
            int bankAccountId,
            Integer yield) {
    }

    public record CreateOwnTransactionRequest(
            double amount,
            String type,
            String description,
            String transactionDate,
            int bankAccountId,
            Integer yield,
            Integer goalId) {
    }

    public record CreateGoalRequest(
            int userId,
            String title,
            double amount,
            double savedAmount,
            String limitDate) {
    }

    public record CreateOwnGoalRequest(String title, double amount, String limitDate) {
    }

    public record CreateTierRequest(String name, int minPointsRequired, int hierarchy) {
    }

    public record CreateRewardRequest(String name, String description, boolean active) {
    }

    public record CreateChallengeRequest(
            String title,
            int minTierId,
            String startDate,
            String endDate,
            boolean active,
            int rewardId,
            int progress) {
    }

    public record CreateCompletedChallengeRequest(int userId, int challengeId, String completedAt) {
    }

    public record LoginRequest(String email, String password) {
    }

    public record LoginResponse(UserResponse user, String token) {
    }

    public record RegisterRequest(
            String username,
            String password,
            String email,
            String celphone,
            int monthlyIncome,
            int monthlySpending) {
    }

    public record ApiError(String message) {
    }

    public record HealthResponse(String status) {
    }

    public record OperationResponse(String message) {
    }

    private static String toIsoDate(Date value) {
        LocalDate date = new java.sql.Date(value.getTime()).toLocalDate();
        return date.toString();
    }
}
