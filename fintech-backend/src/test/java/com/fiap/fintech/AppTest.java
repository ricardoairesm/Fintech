package com.fiap.fintech;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.fintech.api.dto.ApiDtos.TransactionResponse;
import com.fiap.fintech.api.dto.ApiDtos.UserResponse;
import com.fiap.fintech.model.Transaction;
import com.fiap.fintech.model.User;

import junit.framework.TestCase;

import java.sql.Date;

public class AppTest extends TestCase {

    public void testPublicUserResponseNeverExposesPassword() throws Exception {
        Date today = Date.valueOf("2026-05-26");
        User user = new User(1, "Ricardo", "senha-secreta", "ricardo@finup.com", "63000000000",
                1, 100, null, 5000, 1000, today, today);

        String json = new ObjectMapper().writeValueAsString(UserResponse.from(user));

        assertFalse(json.contains("senha-secreta"));
        assertFalse(json.contains("password"));
        assertTrue(json.contains("ricardo@finup.com"));
    }

    public void testTransactionResponseUsesIsoDateForFrontendContract() {
        Date today = Date.valueOf("2026-05-26");
        Transaction transaction = new Transaction(1, 1, 125.50, "Receita", "Deposito",
                today, 1, null, today, today);

        TransactionResponse response = TransactionResponse.from(transaction);

        assertEquals("2026-05-26", response.transactionDate());
    }
}
