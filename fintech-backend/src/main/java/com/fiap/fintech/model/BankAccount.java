package com.fiap.fintech.model;

import java.util.Date;

public class BankAccount {
    private int id;
    private int userId;
    private String bank;
    private String type;
    private String description;
    private String agency;
    private String accountNumber;
    private Date createdAt;
    private Date updatedAt;

    public BankAccount(int id, int userId, String bank, String type, String description,
            String agency, String accountNumber, Date createdAt, Date updatedAt) {
        this.id = id;
        this.userId = userId;
        this.bank = bank;
        this.type = type;
        this.description = description;
        this.agency = agency;
        this.accountNumber = accountNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getBank() { return bank; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public String getAgency() { return agency; }
    public String getAccountNumber() { return accountNumber; }
    public Date getCreatedAt() { return createdAt; }
    public Date getUpdatedAt() { return updatedAt; }

    public void create() {
        System.out.println("Salvaria a conta bancária do usuário no banco de dados.");
    }

    public void update(String newDescription, String newAgency, String newAccountNumber) {
        this.description = newDescription;
        this.agency = newAgency;
        this.accountNumber = newAccountNumber;
        System.out.println("Atualizaria os dados da conta bancária no banco de dados.");
    }

    public void delete() {
        System.out.println("Removeria a conta bancária do banco de dados.");
    }
}
