package com.fiap.fintech.api;

import com.fiap.fintech.api.dto.ApiDtos.ApiError;
import com.fiap.fintech.api.dto.ApiDtos.HealthResponse;
import com.fiap.fintech.api.dto.ApiDtos.LoginRequest;
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

            config.routes.get("/api/tiers", ctx -> ctx.json(service.findTiers()));
            config.routes.get("/api/rewards", ctx -> ctx.json(service.findRewards()));
            config.routes.get("/api/challenges", ctx -> ctx.json(service.findChallenges()));

            config.routes.exception(UnauthorizedException.class, (exception, ctx) -> {
                ctx.status(401);
                ctx.json(new ApiError(exception.getMessage()));
            });
            config.routes.exception(EntidadeNaoEncontrada.class, (exception, ctx) -> {
                ctx.status(404);
                ctx.json(new ApiError("Recurso nao encontrado."));
            });
            config.routes.exception(NumberFormatException.class, (exception, ctx) -> {
                ctx.status(400);
                ctx.json(new ApiError("O identificador informado e invalido."));
            });
            config.routes.exception(SQLException.class, (exception, ctx) -> {
                ctx.status(503);
                ctx.json(new ApiError("Nao foi possivel consultar o banco de dados."));
            });
        });
    }

    private static int userId(Context context) {
        return Integer.parseInt(context.pathParam("userId"));
    }
}
