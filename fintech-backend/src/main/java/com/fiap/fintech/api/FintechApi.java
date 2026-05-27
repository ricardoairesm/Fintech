package com.fiap.fintech.api;

import com.fiap.fintech.api.dto.ApiDtos.ApiError;
import com.fiap.fintech.api.dto.ApiDtos.HealthResponse;
import com.fiap.fintech.api.dto.ApiDtos.LoginRequest;
import com.fiap.fintech.api.dto.ApiDtos.OperationResponse;
import com.fiap.fintech.api.dto.ApiDtos.RegisterRequest;
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
import com.fiap.fintech.config.Environment;
import com.fiap.fintech.exception.EntidadeNaoEncontrada;
import com.fiap.fintech.service.FintechService;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.sql.SQLException;

public final class FintechApi {

    private FintechApi() {
    }

    public static Javalin create(FintechService service) {
        String frontendOrigin = Environment.get("FRONTEND_ORIGIN", "http://localhost:5173");

        return Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> cors.addRule(rule -> rule.allowHost(frontendOrigin)));

            config.routes.beforeMatched(ctx -> ctx.header("Cache-Control", "no-store"));
            config.routes.get("/api/health", ctx -> ctx.json(new HealthResponse("ok")));

            config.routes.post("/api/auth/login", ctx ->
                    ctx.json(service.login(ctx.bodyAsClass(LoginRequest.class))));
            config.routes.post("/api/auth/register", ctx -> {
                service.register(ctx.bodyAsClass(RegisterRequest.class));
                ctx.status(201);
                ctx.json(new OperationResponse("Conta criada com sucesso. Faça login para continuar."));
            });
            config.routes.post("/api/auth/logout", ctx -> {
                service.logout(ctx.header("Authorization"));
                ctx.status(204);
            });

            config.routes.get("/api/users/{userId}", ctx ->
                    ctx.json(service.findUser(userId(ctx))));
            config.routes.get("/api/users/{userId}/address", ctx ->
                    ctx.json(service.findMainAddress(userId(ctx))));
            config.routes.get("/api/users/{userId}/accounts", ctx ->
                    ctx.json(service.findAccounts(userId(ctx))));
            config.routes.get("/api/users/{userId}/transactions", ctx ->
                    ctx.json(service.findTransactions(userId(ctx))));
            config.routes.get("/api/users/{userId}/goals", ctx ->
                    ctx.json(service.findGoals(userId(ctx))));
            config.routes.get("/api/users/{userId}/completed-challenges", ctx ->
                    ctx.json(service.findCompletedChallenges(userId(ctx))));
            config.routes.get("/api/users/{userId}/dashboard", ctx ->
                    ctx.json(service.findDashboard(userId(ctx))));

            config.routes.post("/api/me/accounts", ctx -> {
                int authenticatedUserId = service.requireAuthenticatedUserId(ctx.header("Authorization"));
                service.createOwnBankAccount(authenticatedUserId, ctx.bodyAsClass(CreateOwnBankAccountRequest.class));
                created(ctx);
            });
            config.routes.post("/api/me/goals", ctx -> {
                int authenticatedUserId = service.requireAuthenticatedUserId(ctx.header("Authorization"));
                service.createOwnGoal(authenticatedUserId, ctx.bodyAsClass(CreateOwnGoalRequest.class));
                created(ctx);
            });
            config.routes.post("/api/me/transactions", ctx -> {
                int authenticatedUserId = service.requireAuthenticatedUserId(ctx.header("Authorization"));
                service.createOwnTransaction(authenticatedUserId, ctx.bodyAsClass(CreateOwnTransactionRequest.class));
                created(ctx);
            });

            config.routes.get("/api/tiers", ctx -> ctx.json(service.findTiers()));
            config.routes.get("/api/rewards", ctx -> ctx.json(service.findRewards()));
            config.routes.get("/api/challenges", ctx -> ctx.json(service.findChallenges()));

            config.routes.get("/api/admin/entities", ctx -> {
                service.requireAdmin(ctx.header("Authorization"));
                ctx.json(service.findAdminEntities());
            });
            config.routes.post("/api/admin/users", ctx -> {
                service.requireAdmin(ctx.header("Authorization"));
                service.createUser(ctx.bodyAsClass(CreateUserRequest.class));
                created(ctx);
            });
            config.routes.post("/api/admin/addresses", ctx -> {
                service.requireAdmin(ctx.header("Authorization"));
                service.createAddress(ctx.bodyAsClass(CreateAddressRequest.class));
                created(ctx);
            });
            config.routes.post("/api/admin/accounts", ctx -> {
                service.requireAdmin(ctx.header("Authorization"));
                service.createBankAccount(ctx.bodyAsClass(CreateBankAccountRequest.class));
                created(ctx);
            });
            config.routes.post("/api/admin/transactions", ctx -> {
                service.requireAdmin(ctx.header("Authorization"));
                service.createTransaction(ctx.bodyAsClass(CreateTransactionRequest.class));
                created(ctx);
            });
            config.routes.post("/api/admin/goals", ctx -> {
                service.requireAdmin(ctx.header("Authorization"));
                service.createGoal(ctx.bodyAsClass(CreateGoalRequest.class));
                created(ctx);
            });
            config.routes.post("/api/admin/tiers", ctx -> {
                service.requireAdmin(ctx.header("Authorization"));
                service.createTier(ctx.bodyAsClass(CreateTierRequest.class));
                created(ctx);
            });
            config.routes.post("/api/admin/rewards", ctx -> {
                service.requireAdmin(ctx.header("Authorization"));
                service.createReward(ctx.bodyAsClass(CreateRewardRequest.class));
                created(ctx);
            });
            config.routes.post("/api/admin/challenges", ctx -> {
                service.requireAdmin(ctx.header("Authorization"));
                service.createChallenge(ctx.bodyAsClass(CreateChallengeRequest.class));
                created(ctx);
            });
            config.routes.post("/api/admin/completed-challenges", ctx -> {
                service.requireAdmin(ctx.header("Authorization"));
                service.createCompletedChallenge(ctx.bodyAsClass(CreateCompletedChallengeRequest.class));
                created(ctx);
            });

            config.routes.exception(UnauthorizedException.class, (exception, ctx) -> {
                ctx.status(401);
                ctx.json(new ApiError(exception.getMessage()));
            });
            config.routes.exception(ForbiddenException.class, (exception, ctx) -> {
                ctx.status(403);
                ctx.json(new ApiError(exception.getMessage()));
            });
            config.routes.exception(EntidadeNaoEncontrada.class, (exception, ctx) -> {
                ctx.status(404);
                ctx.json(new ApiError("Recurso não encontrado."));
            });
            config.routes.exception(NumberFormatException.class, (exception, ctx) -> {
                ctx.status(400);
                ctx.json(new ApiError("O identificador informado é inválido."));
            });
            config.routes.exception(InvalidRequestException.class, (exception, ctx) -> {
                ctx.status(400);
                ctx.json(new ApiError(exception.getMessage()));
            });
            config.routes.exception(SQLException.class, (exception, ctx) -> {
                ctx.status(503);
                ctx.json(new ApiError("Não foi possível consultar o banco de dados."));
            });
        });
    }

    private static int userId(Context context) {
        return Integer.parseInt(context.pathParam("userId"));
    }

    private static void created(Context context) {
        context.status(201);
        context.json(new OperationResponse("Registro cadastrado com sucesso."));
    }
}
