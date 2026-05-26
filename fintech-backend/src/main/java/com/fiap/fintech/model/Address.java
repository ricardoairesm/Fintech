package com.fiap.fintech.model;

import java.util.Date;

public class Address {
    private int id;
    private int userId;
    private String addressString;
    private String zipCode;
    private Date createdAt;
    private Date updatedAt;

    public Address(int id, int userId, String addressString, String zipCode, Date createdAt, Date updatedAt) {
        this.id = id;
        this.userId = userId;
        this.addressString = addressString;
        this.zipCode = zipCode;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getAddressString() { return addressString; }
    public String getZipCode() { return zipCode; }
    public Date getCreatedAt() { return createdAt; }
    public Date getUpdatedAt() { return updatedAt; }

    public void create() {
        System.out.println("Salvaria o endereco do usuario no banco de dados.");
    }

    public void update(String newAddressString, String newZipCode) {
        this.addressString = newAddressString;
        this.zipCode = newZipCode;
        System.out.println("Atualizaria o endereco do usuario no banco de dados.");
    }

    public void delete() {
        System.out.println("Removeria o endereco do banco de dados.");
    }
}
