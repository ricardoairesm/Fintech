package com.fiap.fintech.controller;

import com.fiap.fintech.dto.RequestDtos.AddressRequest;
import com.fiap.fintech.dto.RequestDtos.BankAccountRequest;
import com.fiap.fintech.dto.RequestDtos.ChallengeRequest;
import com.fiap.fintech.dto.RequestDtos.CompletedChallengeRequest;
import com.fiap.fintech.dto.RequestDtos.GoalRequest;
import com.fiap.fintech.dto.RequestDtos.RewardRequest;
import com.fiap.fintech.dto.RequestDtos.TierRequest;
import com.fiap.fintech.dto.RequestDtos.TransactionRequest;
import com.fiap.fintech.dto.RequestDtos.UserRequest;
import com.fiap.fintech.dto.ResponseDtos.AdminEntitiesResponse;
import com.fiap.fintech.dto.ResponseDtos.OperationResponse;
import com.fiap.fintech.service.AddressService;
import com.fiap.fintech.service.AuthService;
import com.fiap.fintech.service.BankAccountService;
import com.fiap.fintech.service.ChallengeService;
import com.fiap.fintech.service.CompletedChallengeService;
import com.fiap.fintech.service.DashboardService;
import com.fiap.fintech.service.GoalService;
import com.fiap.fintech.service.RewardService;
import com.fiap.fintech.service.TierService;
import com.fiap.fintech.service.TransactionService;
import com.fiap.fintech.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AuthService authService;
    private final DashboardService dashboardService;
    private final UserService userService;
    private final AddressService addressService;
    private final BankAccountService bankAccountService;
    private final TransactionService transactionService;
    private final GoalService goalService;
    private final TierService tierService;
    private final RewardService rewardService;
    private final ChallengeService challengeService;
    private final CompletedChallengeService completedChallengeService;

    public AdminController(AuthService authService, DashboardService dashboardService, UserService userService,
            AddressService addressService, BankAccountService bankAccountService,
            TransactionService transactionService, GoalService goalService, TierService tierService,
            RewardService rewardService, ChallengeService challengeService,
            CompletedChallengeService completedChallengeService) {
        this.authService = authService;
        this.dashboardService = dashboardService;
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

    @GetMapping("/entities")
    public AdminEntitiesResponse entities(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        authService.requireAdmin(authorization);
        return dashboardService.buildAdminEntities();
    }

    @PostMapping("/users")
    public ResponseEntity<OperationResponse> createUser(@RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody UserRequest request) {
        authService.requireAdmin(authorization);
        userService.create(request);
        return created();
    }

    @PostMapping("/addresses")
    public ResponseEntity<OperationResponse> createAddress(@RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody AddressRequest request) {
        authService.requireAdmin(authorization);
        addressService.create(request);
        return created();
    }

    @PostMapping("/accounts")
    public ResponseEntity<OperationResponse> createAccount(@RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody BankAccountRequest request) {
        authService.requireAdmin(authorization);
        bankAccountService.create(request);
        return created();
    }

    @PostMapping("/transactions")
    public ResponseEntity<OperationResponse> createTransaction(@RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody TransactionRequest request) {
        authService.requireAdmin(authorization);
        transactionService.create(request);
        return created();
    }

    @PostMapping("/goals")
    public ResponseEntity<OperationResponse> createGoal(@RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody GoalRequest request) {
        authService.requireAdmin(authorization);
        goalService.create(request);
        return created();
    }

    @PostMapping("/tiers")
    public ResponseEntity<OperationResponse> createTier(@RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody TierRequest request) {
        authService.requireAdmin(authorization);
        tierService.create(request);
        return created();
    }

    @PostMapping("/rewards")
    public ResponseEntity<OperationResponse> createReward(@RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody RewardRequest request) {
        authService.requireAdmin(authorization);
        rewardService.create(request);
        return created();
    }

    @PostMapping("/challenges")
    public ResponseEntity<OperationResponse> createChallenge(@RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody ChallengeRequest request) {
        authService.requireAdmin(authorization);
        challengeService.create(request);
        return created();
    }

    @PostMapping("/completed-challenges")
    public ResponseEntity<OperationResponse> createCompletedChallenge(@RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody CompletedChallengeRequest request) {
        authService.requireAdmin(authorization);
        completedChallengeService.create(request);
        return created();
    }

    private static ResponseEntity<OperationResponse> created() {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new OperationResponse("Registro cadastrado com sucesso."));
    }
}
