package com.fiap.fintech.model;

import java.util.Date;

public class Transaction {
    private int id;
    private int userId;
    private double amount;
    private String type;
    private String description;
    private Date transactionDate;
    private int bankAccountId;
    private Integer yield;
    private Integer goalId;
    private Date createdAt;
    private Date updatedAt;

    public Transaction(int id, int userId, double amount, String type, String description,
            Date transactionDate, int bankAccountId, Integer yield, Integer goalId, Date createdAt, Date updatedAt) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.transactionDate = transactionDate;
        this.bankAccountId = bankAccountId;
        this.yield = yield;
        this.goalId = goalId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public double getAmount() { return amount; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public Date getTransactionDate() { return transactionDate; }
    public int getBankAccountId() { return bankAccountId; }
    public Integer getYield() { return yield; }
    public Integer getGoalId() { return goalId; }
    public Date getCreatedAt() { return createdAt; }
    public Date getUpdatedAt() { return updatedAt; }

    public void create() {
        System.out.println("Salvaria a transação no banco de dados.");
    }

    public boolean hasYield() {
        return yield != null;
    }
}
