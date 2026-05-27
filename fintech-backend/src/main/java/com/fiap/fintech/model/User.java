package com.fiap.fintech.model;

import java.util.Date;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String celphone;
    private String userType;
    private int tierId;
    private int points;
    private Integer mainAddressId;
    private int monthlyIncome;
    private int monthlySpending;
    private Date createdAt;
    private Date updatedAt;

    public User(int id, String username, String password, String email, String celphone, String userType,
            int tierId, int points, Integer mainAddressId, int monthlyIncome,
            int monthlySpending, Date createdAt, Date updatedAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.celphone = celphone;
        this.userType = userType;
        this.tierId = tierId;
        this.points = points;
        this.mainAddressId = mainAddressId;
        this.monthlyIncome = monthlyIncome;
        this.monthlySpending = monthlySpending;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getCelphone() { return celphone; }
    public String getUserType() { return userType; }
    public int getTierId() { return tierId; }
    public int getPoints() { return points; }
    public Integer getMainAddressId() { return mainAddressId; }
    public int getMonthlyIncome() { return monthlyIncome; }
    public int getMonthlySpending() { return monthlySpending; }
    public Date getCreatedAt() { return createdAt; }
    public Date getUpdatedAt() { return updatedAt; }

    public void register() {
        System.out.println("Salvaria o novo usuário no banco de dados.");
    }

    public void addPoints(int additionalPoints) {
        this.points += additionalPoints;
        System.out.println("Atualizaria a pontuação do usuário.");
    }

    public void updateFinancialProfile(int newMonthlyIncome, int newMonthlySpending) {
        this.monthlyIncome = newMonthlyIncome;
        this.monthlySpending = newMonthlySpending;
        System.out.println("Atualizaria a renda e os gastos mensais do usuário.");
    }
}
