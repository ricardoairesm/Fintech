package com.fiap.fintech.service;

import com.fiap.fintech.dto.ResponseDtos.AdminEntitiesResponse;
import com.fiap.fintech.dto.ResponseDtos.DashboardResponse;
import com.fiap.fintech.dto.ResponseDtos.TransactionResponse;
import com.fiap.fintech.dto.ResponseDtos.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DashboardService {

    private final UserService userService;
    private final AddressService addressService;
    private final BankAccountService bankAccountService;
    private final TransactionService transactionService;
    private final GoalService goalService;
    private final TierService tierService;
    private final RewardService rewardService;
    private final ChallengeService challengeService;
    private final CompletedChallengeService completedChallengeService;

    public DashboardService(UserService userService, AddressService addressService,
            BankAccountService bankAccountService, TransactionService transactionService, GoalService goalService,
            TierService tierService, RewardService rewardService, ChallengeService challengeService,
            CompletedChallengeService completedChallengeService) {
        this.userService = userService;
        this.addressService = addressService;
        this.bankAccountService = bankAccountService;
        this.transactionService = transactionService;
        this.goalService = goalService;
        this.tierService = tierService;
        this.rewardService = rewardService;
        this.challengeService = challengeService;
        this.completedChallengeService = completedChallengeService;
    }

    @Transactional(readOnly = true)
    public DashboardResponse buildDashboard(int userId) {
        UserResponse user = userService.findById(userId);
        List<TransactionResponse> transactions = transactionService.findByUser(userId);
        return new DashboardResponse(
                user,
                addressService.findMainForUser(user.mainAddressId()),
                bankAccountService.findByUser(userId),
                transactions,
                goalService.findByUser(userId),
                tierService.findAll(),
                rewardService.findAll(),
                challengeService.findAll(),
                completedChallengeService.findByUser(userId),
                transactionService.summarize(transactions));
    }

    @Transactional(readOnly = true)
    public AdminEntitiesResponse buildAdminEntities() {
        return new AdminEntitiesResponse(
                userService.findAll(),
                addressService.findAll(),
                bankAccountService.findAll(),
                transactionService.findAll(),
                goalService.findAll(),
                tierService.findAll(),
                rewardService.findAll(),
                challengeService.findAll(),
                completedChallengeService.findAll());
    }
}
