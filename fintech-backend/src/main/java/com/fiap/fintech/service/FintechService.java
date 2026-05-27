package com.fiap.fintech.service;

import com.fiap.fintech.api.ForbiddenException;
import com.fiap.fintech.api.InvalidRequestException;
import com.fiap.fintech.api.UnauthorizedException;
import com.fiap.fintech.api.dto.ApiDtos.AdminEntitiesResponse;
import com.fiap.fintech.api.dto.ApiDtos.AddressResponse;
import com.fiap.fintech.api.dto.ApiDtos.BankAccountResponse;
import com.fiap.fintech.api.dto.ApiDtos.ChallengeResponse;
import com.fiap.fintech.api.dto.ApiDtos.CompletedChallengeResponse;
import com.fiap.fintech.api.dto.ApiDtos.CreateAddressRequest;
import com.fiap.fintech.api.dto.ApiDtos.CreateBankAccountRequest;
import com.fiap.fintech.api.dto.ApiDtos.CreateChallengeRequest;
import com.fiap.fintech.api.dto.ApiDtos.CreateCompletedChallengeRequest;
import com.fiap.fintech.api.dto.ApiDtos.CreateGoalRequest;
import com.fiap.fintech.api.dto.ApiDtos.CreateOwnBankAccountRequest;
import com.fiap.fintech.api.dto.ApiDtos.CreateOwnGoalRequest;
import com.fiap.fintech.api.dto.ApiDtos.CreateOwnTransactionRequest;
import com.fiap.fintech.api.dto.ApiDtos.CreateRewardRequest;
import com.fiap.fintech.api.dto.ApiDtos.CreateTierRequest;
import com.fiap.fintech.api.dto.ApiDtos.CreateTransactionRequest;
import com.fiap.fintech.api.dto.ApiDtos.CreateUserRequest;
import com.fiap.fintech.api.dto.ApiDtos.DashboardResponse;
import com.fiap.fintech.api.dto.ApiDtos.DashboardSummaryResponse;
import com.fiap.fintech.api.dto.ApiDtos.GoalResponse;
import com.fiap.fintech.api.dto.ApiDtos.LoginRequest;
import com.fiap.fintech.api.dto.ApiDtos.LoginResponse;
import com.fiap.fintech.api.dto.ApiDtos.RegisterRequest;
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
import com.fiap.fintech.model.Address;
import com.fiap.fintech.model.BankAccount;
import com.fiap.fintech.model.Challenge;
import com.fiap.fintech.model.CompletedChallenge;
import com.fiap.fintech.model.Goal;
import com.fiap.fintech.model.Reward;
import com.fiap.fintech.model.Tier;
import com.fiap.fintech.model.Transaction;
import com.fiap.fintech.model.User;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FintechService {

    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";
    private final Map<String, Integer> sessions = new ConcurrentHashMap<>();

    public LoginResponse login(LoginRequest request) throws SQLException, UnauthorizedException {
        if (request == null || blank(request.email()) || blank(request.password())) {
            throw new UnauthorizedException("E-mail e senha são obrigatórios.");
        }

        try (UserDao dao = new UserDao()) {
            User user = dao.listar().stream()
                    .filter(candidate -> request.email().equalsIgnoreCase(candidate.getEmail()))
                    .filter(candidate -> request.password().equals(candidate.getPassword()))
                    .findFirst()
                    .orElseThrow(() -> new UnauthorizedException("Credenciais inválidas."));

            String token = UUID.randomUUID().toString();
            sessions.put(token, user.getId());
            return new LoginResponse(UserResponse.from(user), token);
        }
    }

    public void logout(String authorization) {
        String token = bearerToken(authorization);
        if (token != null) {
            sessions.remove(token);
        }
    }

    public void register(RegisterRequest request) throws SQLException, InvalidRequestException {
        requireRequest(request);
        required(request.username(), "Nome do usuário");
        required(request.password(), "Senha");
        required(request.email(), "E-mail");
        required(request.celphone(), "Telefone");

        try (UserDao dao = new UserDao()) {
            boolean emailAlreadyExists = dao.listar().stream()
                    .anyMatch(user -> request.email().equalsIgnoreCase(user.getEmail()));
            if (emailAlreadyExists) {
                throw new InvalidRequestException("Este e-mail já está cadastrado.");
            }
        }

        TierResponse startingTier = findTiers().stream()
                .min(Comparator.comparingInt(TierResponse::hierarchy))
                .orElseThrow(() -> new InvalidRequestException("Nenhum nível inicial foi cadastrado."));
        Date now = new Date();
        try (UserDao dao = new UserDao()) {
            dao.cadastrar(new User(0, request.username(), request.password(), request.email(), request.celphone(),
                    USER, startingTier.id(), 0, null, request.monthlyIncome(), request.monthlySpending(), now, now));
        }
    }

    public void requireAdmin(String authorization) throws SQLException, UnauthorizedException, ForbiddenException {
        int userId = requireAuthenticatedUserId(authorization);

        try (UserDao dao = new UserDao()) {
            User user = dao.pesquisar(userId);
            if (!ADMIN.equalsIgnoreCase(user.getUserType())) {
                throw new ForbiddenException("Acesso permitido apenas para administradores.");
            }
        } catch (EntidadeNaoEncontrada exception) {
            throw new UnauthorizedException("Sessão inválida.");
        }
    }

    public int requireAuthenticatedUserId(String authorization) throws UnauthorizedException {
        String token = bearerToken(authorization);
        Integer userId = token == null ? null : sessions.get(token);
        if (userId == null) {
            throw new UnauthorizedException("Autenticação obrigatória.");
        }
        return userId;
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

    public List<UserResponse> findUsers() throws SQLException {
        try (UserDao dao = new UserDao()) {
            return dao.listar().stream().map(UserResponse::from).toList();
        }
    }

    public List<AddressResponse> findAddresses() throws SQLException {
        try (AddressDao dao = new AddressDao()) {
            return dao.listar().stream().map(AddressResponse::from).toList();
        }
    }

    public List<BankAccountResponse> findAllAccounts() throws SQLException {
        try (BankAccountDao dao = new BankAccountDao()) {
            return dao.listar().stream().map(BankAccountResponse::from).toList();
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

    public List<TransactionResponse> findAllTransactions() throws SQLException {
        try (TransactionDao dao = new TransactionDao()) {
            return dao.listar().stream().map(TransactionResponse::from).toList();
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

    public List<GoalResponse> findAllGoals() throws SQLException {
        try (GoalDao dao = new GoalDao()) {
            return dao.listar().stream().map(GoalResponse::from).toList();
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

    public List<CompletedChallengeResponse> findAllCompletedChallenges() throws SQLException {
        try (CompletedChallengeDao dao = new CompletedChallengeDao()) {
            return dao.listar().stream().map(CompletedChallengeResponse::from).toList();
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
        List<TransactionResponse> transactions = findTransactions(userId);

        return new DashboardResponse(
                user,
                address,
                findAccounts(userId),
                transactions,
                findGoals(userId),
                findTiers(),
                findRewards(),
                findChallenges(),
                findCompletedChallenges(userId),
                summarize(transactions));
    }

    private static DashboardSummaryResponse summarize(List<TransactionResponse> transactions) {
        double totalIncome = sumByType(transactions, "Receita");
        double totalExpense = sumByType(transactions, "Despesa");
        double totalInvested = sumByType(transactions, "Investimento");
        return new DashboardSummaryResponse(totalIncome - totalExpense - totalInvested,
                totalIncome, totalExpense, totalInvested);
    }

    private static double sumByType(List<TransactionResponse> transactions, String type) {
        return transactions.stream()
                .filter(transaction -> type.equalsIgnoreCase(transaction.type()))
                .mapToDouble(TransactionResponse::amount)
                .sum();
    }

    public AdminEntitiesResponse findAdminEntities() throws SQLException {
        return new AdminEntitiesResponse(
                findUsers(),
                findAddresses(),
                findAllAccounts(),
                findAllTransactions(),
                findAllGoals(),
                findTiers(),
                findRewards(),
                findChallenges(),
                findAllCompletedChallenges());
    }

    public void createUser(CreateUserRequest request) throws SQLException, InvalidRequestException {
        requireRequest(request);
        required(request.username(), "Nome do usuário");
        required(request.password(), "Senha");
        required(request.email(), "E-mail");
        required(request.celphone(), "Telefone");
        String userType = requiredUserType(request.userType());
        positive(request.tierId(), "Nível");
        Date now = new Date();
        try (UserDao dao = new UserDao()) {
            dao.cadastrar(new User(0, request.username(), request.password(), request.email(), request.celphone(),
                    userType, request.tierId(), request.points(), request.mainAddressId(), request.monthlyIncome(),
                    request.monthlySpending(), now, now));
        }
    }

    public void createAddress(CreateAddressRequest request) throws SQLException, InvalidRequestException {
        requireRequest(request);
        positive(request.userId(), "Usuário");
        required(request.addressString(), "Endereço");
        required(request.zipCode(), "CEP");
        Date now = new Date();
        try (AddressDao dao = new AddressDao()) {
            dao.cadastrar(new Address(0, request.userId(), request.addressString(), request.zipCode(), now, now));
        }
    }

    public void createBankAccount(CreateBankAccountRequest request) throws SQLException, InvalidRequestException {
        requireRequest(request);
        positive(request.userId(), "Usuário");
        required(request.bank(), "Banco");
        required(request.type(), "Tipo");
        required(request.description(), "Descrição");
        required(request.agency(), "Agência");
        required(request.accountNumber(), "Conta");
        Date now = new Date();
        try (BankAccountDao dao = new BankAccountDao()) {
            dao.cadastrar(new BankAccount(0, request.userId(), request.bank(), request.type(), request.description(),
                    request.agency(), request.accountNumber(), now, now));
        }
    }

    public void createOwnBankAccount(int userId, CreateOwnBankAccountRequest request)
            throws SQLException, InvalidRequestException {
        requireRequest(request);
        required(request.bank(), "Banco");
        required(request.type(), "Tipo");
        required(request.description(), "Descrição");
        required(request.agency(), "Agência");
        required(request.accountNumber(), "Conta");
        Date now = new Date();
        try (BankAccountDao dao = new BankAccountDao()) {
            dao.cadastrar(new BankAccount(0, userId, request.bank(), request.type(), request.description(),
                    request.agency(), request.accountNumber(), now, now));
        }
    }

    public void createTransaction(CreateTransactionRequest request) throws SQLException, InvalidRequestException {
        requireRequest(request);
        positive(request.userId(), "Usuário");
        positive(request.bankAccountId(), "Conta bancária");
        required(request.type(), "Tipo");
        required(request.description(), "Descrição");
        Date now = new Date();
        try (TransactionDao dao = new TransactionDao()) {
            dao.cadastrar(new Transaction(0, request.userId(), request.amount(), request.type(), request.description(),
                    date(request.transactionDate(), "Data da transação"), request.bankAccountId(), request.yield(),
                    null, now, now));
        }
    }

    public void createOwnTransaction(int userId, CreateOwnTransactionRequest request)
            throws SQLException, InvalidRequestException {
        requireRequest(request);
        if (request.amount() <= 0) {
            throw new InvalidRequestException("Valor deve ser maior que zero.");
        }
        required(request.type(), "Tipo");
        required(request.description(), "Descrição");
        positive(request.bankAccountId(), "Conta bancária");
        boolean ownsAccount = findAccounts(userId).stream()
                .anyMatch(account -> account.id() == request.bankAccountId());
        if (!ownsAccount) {
            throw new InvalidRequestException("A conta bancária selecionada não pertence ao usuário autenticado.");
        }

        Integer goalId = request.goalId();
        Goal targetGoal = goalId == null ? null : ownedGoal(userId, goalId);

        Date now = new Date();
        try (TransactionDao dao = new TransactionDao()) {
            dao.cadastrar(new Transaction(0, userId, request.amount(), request.type(), request.description(),
                    date(request.transactionDate(), "Data da transação"), request.bankAccountId(), request.yield(),
                    goalId, now, now));
        }

        if (targetGoal != null && "Investimento".equalsIgnoreCase(request.type())) {
            try (GoalDao dao = new GoalDao()) {
                Goal updated = new Goal(targetGoal.getId(), targetGoal.getUserId(), targetGoal.getTitle(),
                        targetGoal.getAmount(), targetGoal.getSavedAmount() + request.amount(),
                        targetGoal.getLimitDate(), targetGoal.getCreatedAt(), now);
                dao.atualizar(updated);
            } catch (EntidadeNaoEncontrada exception) {
                throw new InvalidRequestException("Meta não encontrada.");
            }
        }
    }

    private Goal ownedGoal(int userId, int goalId) throws SQLException, InvalidRequestException {
        try (GoalDao dao = new GoalDao()) {
            Goal goal = dao.pesquisar(goalId);
            if (goal.getUserId() != userId) {
                throw new InvalidRequestException("A meta selecionada não pertence ao usuário autenticado.");
            }
            return goal;
        } catch (EntidadeNaoEncontrada exception) {
            throw new InvalidRequestException("Meta não encontrada.");
        }
    }

    public void createGoal(CreateGoalRequest request) throws SQLException, InvalidRequestException {
        requireRequest(request);
        positive(request.userId(), "Usuário");
        required(request.title(), "Título");
        Date now = new Date();
        try (GoalDao dao = new GoalDao()) {
            dao.cadastrar(new Goal(0, request.userId(), request.title(), request.amount(), request.savedAmount(),
                    date(request.limitDate(), "Data limite"), now, now));
        }
    }

    public void createOwnGoal(int userId, CreateOwnGoalRequest request) throws SQLException, InvalidRequestException {
        requireRequest(request);
        required(request.title(), "Título");
        if (request.amount() <= 0) {
            throw new InvalidRequestException("Valor alvo deve ser maior que zero.");
        }
        Date now = new Date();
        try (GoalDao dao = new GoalDao()) {
            dao.cadastrar(new Goal(0, userId, request.title(), request.amount(), 0,
                    date(request.limitDate(), "Data limite"), now, now));
        }
    }

    public void createTier(CreateTierRequest request) throws SQLException, InvalidRequestException {
        requireRequest(request);
        required(request.name(), "Nome");
        Date now = new Date();
        try (TierDao dao = new TierDao()) {
            dao.cadastrar(new Tier(0, request.name(), request.minPointsRequired(), request.hierarchy(), now, now));
        }
    }

    public void createReward(CreateRewardRequest request) throws SQLException, InvalidRequestException {
        requireRequest(request);
        required(request.name(), "Nome");
        required(request.description(), "Descrição");
        Date now = new Date();
        try (RewardDao dao = new RewardDao()) {
            dao.cadastrar(new Reward(0, request.name(), request.description(), request.active(), now, now));
        }
    }

    public void createChallenge(CreateChallengeRequest request) throws SQLException, InvalidRequestException {
        requireRequest(request);
        required(request.title(), "Título");
        positive(request.minTierId(), "Nível mínimo");
        positive(request.rewardId(), "Recompensa");
        if (request.progress() < 0 || request.progress() > 100) {
            throw new InvalidRequestException("Progresso deve estar entre 0 e 100.");
        }
        Date now = new Date();
        try (ChallengeDao dao = new ChallengeDao()) {
            dao.cadastrar(new Challenge(0, request.title(), request.minTierId(), date(request.startDate(), "Início"),
                    date(request.endDate(), "Fim"), request.active(), request.rewardId(), request.progress(),
                    now, now));
        }
    }

    public void createCompletedChallenge(CreateCompletedChallengeRequest request)
            throws SQLException, InvalidRequestException {
        requireRequest(request);
        positive(request.userId(), "Usuário");
        positive(request.challengeId(), "Desafio");
        Date now = new Date();
        try (CompletedChallengeDao dao = new CompletedChallengeDao()) {
            dao.cadastrar(new CompletedChallenge(0, request.userId(), request.challengeId(),
                    date(request.completedAt(), "Conclusão"), now, now));
        }
    }

    private static void requireRequest(Object request) throws InvalidRequestException {
        if (request == null) {
            throw new InvalidRequestException("Dados do cadastro são obrigatórios.");
        }
    }

    private static void required(String value, String field) throws InvalidRequestException {
        if (blank(value)) {
            throw new InvalidRequestException(field + " é obrigatório.");
        }
    }

    private static void positive(int value, String field) throws InvalidRequestException {
        if (value <= 0) {
            throw new InvalidRequestException(field + " deve ser informado.");
        }
    }

    private static Date date(String value, String field) throws InvalidRequestException {
        required(value, field);
        try {
            return java.sql.Date.valueOf(value);
        } catch (IllegalArgumentException exception) {
            throw new InvalidRequestException(field + " deve estar no formato AAAA-MM-DD.");
        }
    }

    private static String requiredUserType(String value) throws InvalidRequestException {
        required(value, "Tipo do usuário");
        String userType = value.toUpperCase();
        if (!USER.equals(userType) && !ADMIN.equals(userType)) {
            throw new InvalidRequestException("Tipo do usuário deve ser USER ou ADMIN.");
        }
        return userType;
    }

    private static String bearerToken(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return null;
        }
        return authorization.substring("Bearer ".length()).trim();
    }

    private static boolean blank(String value) {
        return value == null || value.isBlank();
    }
}
