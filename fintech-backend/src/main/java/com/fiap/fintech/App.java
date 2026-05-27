package com.fiap.fintech;

import com.fiap.fintech.api.FintechApi;
import com.fiap.fintech.config.Environment;
import com.fiap.fintech.service.FintechService;

import io.javalin.Javalin;

public class App {
    public static void main(String[] args) {
        int port = Integer.parseInt(Environment.get("PORT", "8080"));
        Javalin api = FintechApi.create(new FintechService());
        api.start(port);
        System.out.println("Fintech API disponível em http://localhost:" + port + "/api");
    }
}
