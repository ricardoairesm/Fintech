package com.fiap.fintech.dto;

import com.fiap.fintech.entity.Address;
import com.fiap.fintech.entity.BankAccount;
import com.fiap.fintech.entity.Challenge;
import com.fiap.fintech.entity.CompletedChallenge;
import com.fiap.fintech.entity.Goal;
import com.fiap.fintech.entity.Reward;
import com.fiap.fintech.entity.Tier;
import com.fiap.fintech.entity.Transaction;
import com.fiap.fintech.entity.User;

import java.time.LocalDate;
import java.util.List;

public final class ResponseDtos {

    private ResponseDtos() {
    }

    public record UserResponse(
            Integer id,
            String username,
            String email,
            String celphone,
            String userType,
            Integer tierId,
            Integer points,
            Integer mainAddressId,
            Integer monthlyIncome,
            Integer monthlySpending) {

        public static UserResponse from(User user) {
            return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getCelphone(),
                    user.getUserType(), user.getTierId(), user.getPoints(), user.getMainAddressId(),
                    user.getMonthlyIncome(), user.getMonthlySpending());
        }
    }

    public record AddressResponse(Integer id, Integer userId, String addressString, String zipCode) {
        public static AddressResponse from(Address address) {
            return new AddressResponse(address.getId(), address.getUserId(), address.getAddressString(),
                    address.getZipCode());
        }
    }

    public record BankAccountResponse(
            Integer id,
            Integer userId,
            String bank,
            String type,
            String description,
            String agency,
            String accountNumber) {

        public static BankAccountResponse from(BankAccount account) {
            return new BankAccountResponse(account.getId(), account.getUserId(), account.getBank(), account.getType(),
                    account.getDescription(), account.getAgency(), account.getAccountNumber());
        }
    }

    public record TransactionResponse(
            Integer id,
            Integer userId,
            Double amount,
            String type,
            String description,
            LocalDate transactionDate,
            Integer bankAccountId,
            Integer yield,
            Integer goalId) {

        public static TransactionResponse from(Transaction transaction) {
            return new TransactionResponse(transaction.getId(), transaction.getUserId(), transaction.getAmount(),
                    transaction.getType(), transaction.getDescription(), transaction.getTransactionDate(),
                    transaction.getBankAccountId(), transaction.getYield(), transaction.getGoalId());
        }
    }

    public record GoalResponse(
            Integer id,
            Integer userId,
            String title,
            Double amount,
            Double savedAmount,
            LocalDate limitDate) {

        public static GoalResponse from(Goal goal) {
            return new GoalResponse(goal.getId(), goal.getUserId(), goal.getTitle(), goal.getAmount(),
                    goal.getSavedAmount(), goal.getLimitDate());
        }
    }

    public record TierResponse(Integer id, String name, Integer minPointsRequired, Integer hierarchy) {
        public static TierResponse from(Tier tier) {
            return new TierResponse(tier.getId(), tier.getName(), tier.getMinPointsRequired(), tier.getHierarchy());
        }
    }

    public record RewardResponse(Integer id, String name, String description, Boolean active) {
        public static RewardResponse from(Reward reward) {
            return new RewardResponse(reward.getId(), reward.getName(), reward.getDescription(), reward.getActive());
        }
    }

    public record ChallengeResponse(
            Integer id,
            String title,
            Integer minTierId,
            LocalDate startDate,
            LocalDate endDate,
            Boolean active,
            Integer rewardId,
            Integer progress) {

        public static ChallengeResponse from(Challenge challenge) {
            return new ChallengeResponse(challenge.getId(), challenge.getTitle(), challenge.getMinTierId(),
                    challenge.getStartDate(), challenge.getEndDate(), challenge.getActive(), challenge.getRewardId(),
                    challenge.getProgress());
        }
    }

    public record CompletedChallengeResponse(Integer id, Integer userId, Integer challengeId, LocalDate completedAt) {
        public static CompletedChallengeResponse from(CompletedChallenge challenge) {
            return new CompletedChallengeResponse(challenge.getId(), challenge.getUserId(), challenge.getChallengeId(),
                    challenge.getCompletedAt());
        }
    }

    public record DashboardSummaryResponse(double balance, double totalIncome, double totalExpense,
            double totalInvested) {
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

    public record LoginResponse(UserResponse user, String token) {
    }

    public record OperationResponse(String message) {
    }

    public record ApiError(String message) {
    }
}
