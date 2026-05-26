package com.fiap.fintech;

import com.fiap.fintech.api.FintechApi;
import com.fiap.fintech.service.FintechService;

import io.javalin.Javalin;

public class App {
    public static void main(String[] args) {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        Javalin api = FintechApi.create(new FintechService());
        api.start(port);
        System.out.println("Fintech API disponivel em http://localhost:" + port + "/api");
    }
}
