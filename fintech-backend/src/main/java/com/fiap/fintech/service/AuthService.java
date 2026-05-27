package com.fiap.fintech.service;

import com.fiap.fintech.dto.RequestDtos.LoginRequest;
import com.fiap.fintech.dto.RequestDtos.RegisterRequest;
import com.fiap.fintech.dto.ResponseDtos.LoginResponse;
import com.fiap.fintech.dto.ResponseDtos.UserResponse;
import com.fiap.fintech.entity.Tier;
import com.fiap.fintech.entity.User;
import com.fiap.fintech.exception.ForbiddenException;
import com.fiap.fintech.exception.InvalidRequestException;
import com.fiap.fintech.exception.UnauthorizedException;
import com.fiap.fintech.repository.TierRepository;
import com.fiap.fintech.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {

    private static final String USER = "USER";
    private static final String ADMIN = "ADMIN";

    private final UserRepository userRepository;
    private final TierRepository tierRepository;
    private final Map<String, Integer> sessions = new ConcurrentHashMap<>();

    public AuthService(UserRepository userRepository, TierRepository tierRepository) {
        this.userRepository = userRepository;
        this.tierRepository = tierRepository;
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        if (request == null || isBlank(request.email()) || isBlank(request.password())) {
            throw new UnauthorizedException("E-mail e senha são obrigatórios.");
        }
        User user = userRepository.findByEmailIgnoreCase(request.email())
                .filter(candidate -> request.password().equals(candidate.getPassword()))
                .orElseThrow(() -> new UnauthorizedException("Credenciais inválidas."));

        String token = UUID.randomUUID().toString();
        sessions.put(token, user.getId());
        return new LoginResponse(UserResponse.from(user), token);
    }

    @Transactional
    public void register(RegisterRequest request) {
        if (request == null) {
            throw new InvalidRequestException("Requisição inválida.");
        }
        require(request.username(), "Nome do usuário");
        require(request.password(), "Senha");
        require(request.email(), "E-mail");
        require(request.celphone(), "Telefone");

        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new InvalidRequestException("Este e-mail já está cadastrado.");
        }
        if (userRepository.existsByUsernameIgnoreCase(request.username())) {
            throw new InvalidRequestException("Este nome de usuário já está em uso.");
        }

        Tier startingTier = tierRepository.findAll().stream()
                .min(Comparator.comparingInt(Tier::getHierarchy))
                .orElseThrow(() -> new InvalidRequestException("Nenhum nível inicial foi cadastrado."));

        LocalDate now = LocalDate.now();
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(request.password());
        user.setEmail(request.email());
        user.setCelphone(request.celphone());
        user.setUserType(USER);
        user.setTierId(startingTier.getId());
        user.setPoints(0);
        user.setMainAddressId(null);
        user.setMonthlyIncome(request.monthlyIncome() == null ? 0 : request.monthlyIncome());
        user.setMonthlySpending(request.monthlySpending() == null ? 0 : request.monthlySpending());
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        userRepository.save(user);
    }

    public void logout(String authorization) {
        String token = bearerToken(authorization);
        if (token != null) {
            sessions.remove(token);
        }
    }

    public int requireAuthenticatedUserId(String authorization) {
        String token = bearerToken(authorization);
        Integer userId = token == null ? null : sessions.get(token);
        if (userId == null) {
            throw new UnauthorizedException("Autenticação obrigatória.");
        }
        return userId;
    }

    @Transactional(readOnly = true)
    public void requireAdmin(String authorization) {
        int userId = requireAuthenticatedUserId(authorization);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("Sessão inválida."));
        if (!ADMIN.equalsIgnoreCase(user.getUserType())) {
            throw new ForbiddenException("Acesso permitido apenas para administradores.");
        }
    }

    private static String bearerToken(String authorization) {
        if (authorization == null) {
            return null;
        }
        String prefix = "Bearer ";
        if (authorization.regionMatches(true, 0, prefix, 0, prefix.length())) {
            return authorization.substring(prefix.length()).trim();
        }
        return null;
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private static void require(String value, String field) {
        if (isBlank(value)) {
            throw new InvalidRequestException(field + " é obrigatório.");
        }
    }
}
