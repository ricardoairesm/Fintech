package com.fiap.fintech.service;

import com.fiap.fintech.api.UnauthorizedException;
import com.fiap.fintech.api.dto.ApiDtos.AddressResponse;
import com.fiap.fintech.api.dto.ApiDtos.BankAccountResponse;
import com.fiap.fintech.api.dto.ApiDtos.ChallengeResponse;
import com.fiap.fintech.api.dto.ApiDtos.CompletedChallengeResponse;
import com.fiap.fintech.api.dto.ApiDtos.DashboardResponse;
import com.fiap.fintech.api.dto.ApiDtos.GoalResponse;
import com.fiap.fintech.api.dto.ApiDtos.LoginRequest;
import com.fiap.fintech.api.dto.ApiDtos.LoginResponse;
import com.fiap.fintech.api.dto.ApiDtos.RewardResponse;
import com.fiap.fintech.api.dto.ApiDtos.TierResponse;
import com.fiap.fintech.api.dto.ApiDtos.TransactionResponse;
import com.fiap.fintech.api.dto.ApiDtos.UserResponse;
import com.fiap.fintech.dao.AddressDao;
import com.fiap.fintech.dao.BankAccountDao;
import com.fiap.fintech.dao.ChallengeDao;
import com.fiap.fintech.dao.CompletedChallengeDao;
import com.fiap.fintech.dao.GoalDao;
import com.fiap.fintech.dao.RewardDao;
import com.fiap.fintech.dao.TierDao;
import com.fiap.fintech.dao.TransactionDao;
import com.fiap.fintech.dao.UserDao;
import com.fiap.fintech.exception.EntidadeNaoEncontrada;
import com.fiap.fintech.model.User;

import java.sql.SQLException;
import java.util.List;

public class FintechService {

    public LoginResponse login(LoginRequest request) throws SQLException, UnauthorizedException {
        if (request == null || blank(request.email()) || blank(request.password())) {
            throw new UnauthorizedException("E-mail e senha sao obrigatorios.");
        }

        try (UserDao dao = new UserDao()) {
            User user = dao.listar().stream()
                    .filter(candidate -> request.email().equalsIgnoreCase(candidate.getEmail()))
                    .filter(candidate -> request.password().equals(candidate.getPassword()))
                    .findFirst()
                    .orElseThrow(() -> new UnauthorizedException("Credenciais invalidas."));

            return new LoginResponse(UserResponse.from(user));
        }
    }

    public UserResponse findUser(int userId) throws SQLException, EntidadeNaoEncontrada {
        try (UserDao dao = new UserDao()) {
            return UserResponse.from(dao.pesquisar(userId));
        }
    }

    public AddressResponse findMainAddress(int userId) throws SQLException, EntidadeNaoEncontrada {
        UserResponse user = findUser(userId);
        if (user.mainAddressId() == null) {
            throw new EntidadeNaoEncontrada();
        }
        return findAddress(user.mainAddressId());
    }

    private AddressResponse findAddress(int addressId) throws SQLException, EntidadeNaoEncontrada {
        try (AddressDao dao = new AddressDao()) {
            return AddressResponse.from(dao.pesquisar(addressId));
        }
    }

    public List<BankAccountResponse> findAccounts(int userId) throws SQLException {
        try (BankAccountDao dao = new BankAccountDao()) {
            return dao.listar().stream()
                    .filter(item -> item.getUserId() == userId)
                    .map(BankAccountResponse::from)
                    .toList();
        }
    }

    public List<TransactionResponse> findTransactions(int userId) throws SQLException {
        try (TransactionDao dao = new TransactionDao()) {
            return dao.listar().stream()
                    .filter(item -> item.getUserId() == userId)
                    .map(TransactionResponse::from)
                    .toList();
        }
    }

    public List<GoalResponse> findGoals(int userId) throws SQLException {
        try (GoalDao dao = new GoalDao()) {
            return dao.listar().stream()
                    .filter(item -> item.getUserId() == userId)
                    .map(GoalResponse::from)
                    .toList();
        }
    }

    public List<CompletedChallengeResponse> findCompletedChallenges(int userId) throws SQLException {
        try (CompletedChallengeDao dao = new CompletedChallengeDao()) {
            return dao.listar().stream()
                    .filter(item -> item.getUserId() == userId)
                    .map(CompletedChallengeResponse::from)
                    .toList();
        }
    }

    public List<TierResponse> findTiers() throws SQLException {
        try (TierDao dao = new TierDao()) {
            return dao.listar().stream().map(TierResponse::from).toList();
        }
    }

    public List<RewardResponse> findRewards() throws SQLException {
        try (RewardDao dao = new RewardDao()) {
            return dao.listar().stream().map(RewardResponse::from).toList();
        }
    }

    public List<ChallengeResponse> findChallenges() throws SQLException {
        try (ChallengeDao dao = new ChallengeDao()) {
            return dao.listar().stream().map(ChallengeResponse::from).toList();
        }
    }

    public DashboardResponse findDashboard(int userId) throws SQLException, EntidadeNaoEncontrada {
        UserResponse user = findUser(userId);
        AddressResponse address = user.mainAddressId() == null ? null : findAddress(user.mainAddressId());

        return new DashboardResponse(
                user,
                address,
                findAccounts(userId),
                findTransactions(userId),
                findGoals(userId),
                findTiers(),
                findRewards(),
                findChallenges(),
                findCompletedChallenges(userId));
    }

    private static boolean blank(String value) {
        return value == null || value.isBlank();
    }
}
