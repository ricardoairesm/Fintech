package com.fiap.fintech.config;

import io.github.cdimascio.dotenv.Dotenv;

import java.nio.file.Files;
import java.nio.file.Path;

public final class Environment {

    private static final Dotenv DOTENV = Dotenv.configure()
            .directory(findEnvironmentDirectory())
            .ignoreIfMissing()
            .load();

    private Environment() {
    }

    public static String get(String key, String defaultValue) {
        String value = DOTENV.get(key);
        return value == null || value.isBlank() ? defaultValue : value;
    }

    public static String require(String key) {
        String value = DOTENV.get(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Configure " + key + " no arquivo .env ou nas variaveis de ambiente.");
        }
        return value;
    }

    private static String findEnvironmentDirectory() {
        if (Files.exists(Path.of(".env"))) {
            return ".";
        }
        if (Files.exists(Path.of("fintech-backend", ".env"))) {
            return "fintech-backend";
        }
        return ".";
    }
}
