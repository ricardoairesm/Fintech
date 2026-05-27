package com.fiap.fintech.model;

import java.util.Date;

public class Goal {
    private int id;
    private int userId;
    private String title;
    private double amount;
    private double savedAmount;
    private Date limitDate;
    private Date createdAt;
    private Date updatedAt;

    public Goal(int id, int userId, double amount, Date limitDate, Date createdAt, Date updatedAt) {
        this(id, userId, "Meta financeira", amount, 0, limitDate, createdAt, updatedAt);
    }

    public Goal(int id, int userId, String title, double amount, double savedAmount,
            Date limitDate, Date createdAt, Date updatedAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.amount = amount;
        this.savedAmount = savedAmount;
        this.limitDate = limitDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getTitle() { return title; }
    public double getAmount() { return amount; }
    public double getSavedAmount() { return savedAmount; }
    public Date getLimitDate() { return limitDate; }
    public Date getCreatedAt() { return createdAt; }
    public Date getUpdatedAt() { return updatedAt; }

    public void create() {
        System.out.println("Salvaria a meta financeira do usuário no banco de dados.");
    }

    public void updateAmount(double newAmount) {
        this.amount = newAmount;
        System.out.println("Atualizaria o valor da meta no banco de dados.");
    }
}
