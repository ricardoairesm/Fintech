package com.fiap.fintech.controller;

import com.fiap.fintech.dto.RequestDtos.UserRequest;
import com.fiap.fintech.dto.ResponseDtos.AddressResponse;
import com.fiap.fintech.dto.ResponseDtos.BankAccountResponse;
import com.fiap.fintech.dto.ResponseDtos.CompletedChallengeResponse;
import com.fiap.fintech.dto.ResponseDtos.DashboardResponse;
import com.fiap.fintech.dto.ResponseDtos.GoalResponse;
import com.fiap.fintech.dto.ResponseDtos.TransactionResponse;
import com.fiap.fintech.dto.ResponseDtos.UserResponse;
import com.fiap.fintech.exception.NotFoundException;
import com.fiap.fintech.service.AddressService;
import com.fiap.fintech.service.BankAccountService;
import com.fiap.fintech.service.CompletedChallengeService;
import com.fiap.fintech.service.DashboardService;
import com.fiap.fintech.service.GoalService;
import com.fiap.fintech.service.TransactionService;
import com.fiap.fintech.service.UserService;
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
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final DashboardService dashboardService;
    private final AddressService addressService;
    private final BankAccountService bankAccountService;
    private final TransactionService transactionService;
    private final GoalService goalService;
    private final CompletedChallengeService completedChallengeService;

    public UserController(UserService userService, DashboardService dashboardService, AddressService addressService,
            BankAccountService bankAccountService, TransactionService transactionService, GoalService goalService,
            CompletedChallengeService completedChallengeService) {
        this.userService = userService;
        this.dashboardService = dashboardService;
        this.addressService = addressService;
        this.bankAccountService = bankAccountService;
        this.transactionService = transactionService;
        this.goalService = goalService;
        this.completedChallengeService = completedChallengeService;
    }

    @GetMapping
    public List<UserResponse> list() {
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public UserResponse getById(@PathVariable int userId) {
        return userService.findById(userId);
    }

    @GetMapping("/{userId}/dashboard")
    public DashboardResponse dashboard(@PathVariable int userId) {
        return dashboardService.buildDashboard(userId);
    }

    @GetMapping("/{userId}/address")
    public AddressResponse address(@PathVariable int userId) {
        Integer mainAddressId = userService.findById(userId).mainAddressId();
        AddressResponse address = addressService.findMainForUser(mainAddressId);
        if (address == null) {
            throw new NotFoundException("Endereço principal não encontrado.");
        }
        return address;
    }

    @GetMapping("/{userId}/accounts")
    public List<BankAccountResponse> accounts(@PathVariable int userId) {
        return bankAccountService.findByUser(userId);
    }

    @GetMapping("/{userId}/transactions")
    public List<TransactionResponse> transactions(@PathVariable int userId) {
        return transactionService.findByUser(userId);
    }

    @GetMapping("/{userId}/goals")
    public List<GoalResponse> goals(@PathVariable int userId) {
        return goalService.findByUser(userId);
    }

    @GetMapping("/{userId}/completed-challenges")
    public List<CompletedChallengeResponse> completedChallenges(@PathVariable int userId) {
        return completedChallengeService.findByUser(userId);
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @PutMapping("/{userId}")
    public UserResponse update(@PathVariable int userId, @RequestBody UserRequest request) {
        return userService.update(userId, request);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable int userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}
